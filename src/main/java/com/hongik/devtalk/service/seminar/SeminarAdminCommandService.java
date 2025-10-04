package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.*;
import com.hongik.devtalk.domain.enums.SeminarStatus;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarRegisterRequestDTO;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarInfoResponseDTO;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarUpdateRequestDTO;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.SessionRepository;
import com.hongik.devtalk.repository.live.LiveRepository;
import com.hongik.devtalk.repository.liveFile.LiveFileRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import com.hongik.devtalk.repository.speaker.SpeakerRepository;
import com.hongik.devtalk.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeminarAdminCommandService {

    private final SeminarRepository seminarRepository;
    private final SpeakerRepository speakerRepository;
    private final SessionRepository sessionRepository;
    private final LiveFileRepository liveFileRepository;
    private final LiveRepository liveRepository;
    private final S3Service s3Service;

    /**
     * 세미나 등록
     *
     * @param request 세미나 등록 요청 DTO
     * @param thumbnailFile 세미나 대표 썸네일
     * @param materials 세미나 자료 파일 리스트
     * @param speakerProfiles 연사 프로필 이미지 리스트
     * @return 등록된 세미나 정보 DTO
     * @throws GeneralException 세미나 회차 검증, 기간 검증, 파일 검증 실패 시
     */
    @Transactional
    public SeminarInfoResponseDTO registerSeminar(
            SeminarRegisterRequestDTO request,
            MultipartFile thumbnailFile,
            List<MultipartFile> materials,
            List<MultipartFile> speakerProfiles
    ) {
        // 회차 중복 검증
        if (seminarRepository.existsBySeminarNum(request.getSeminarNum())) {
            throw new GeneralException(GeneralErrorCode.SEMINAR_NUM_ALREADY_EXISTS);
        }

        // 세미나 기간 검증
        validatePeriods(
                request.getActiveStartDate(), request.getActiveEndDate(),
                request.getApplyStartDate(), request.getApplyEndDate()
        );

        // 활성화 기간이 겹치는 세미나가 있는지 검증
        if (seminarRepository.overlapsSeminar(request.getActiveStartDate(), request.getActiveEndDate())) {
            throw new GeneralException(GeneralErrorCode.SEMINAR_ACTIVE_PERIOD_OVERLAP);
        }

        // 연사 수와 프로필 사진 수 일치하는지 검증
        validateSpeakersAndProfiles(request.getSpeakers().size(), speakerProfiles);

        // 세미나 정보
        // 썸네일 이미지 타입 검증
        if (!s3Service.isValidImageFile(thumbnailFile)) {
            throw new GeneralException(GeneralErrorCode.UNSUPPORTED_FILE_TYPE);
        }

        // S3 업로드
        String thumbnailUrl = s3Service.uploadFile(thumbnailFile, "seminar/thumbnail");

        // 세미나 저장
        Seminar seminar = Seminar.builder()
                .seminarNum(request.getSeminarNum())
                .topic(request.getTopic())
                .seminarDate(request.getSeminarDate())
                .place(request.getPlace())
                .activeStartDate(request.getActiveStartDate())
                .activeEndDate(request.getActiveEndDate())
                .startDate(request.getApplyStartDate())
                .endDate(request.getApplyEndDate())
                .thumbnailUrl(thumbnailUrl)
                .thumbnailFileName(getFileName(thumbnailFile.getOriginalFilename()))
                .thumbnailFileExtension(getExtension(thumbnailFile.getOriginalFilename()))
                .thumbnailFileSize(thumbnailFile.getSize())
                .build();
        seminarRepository.save(seminar);

        // 세미나 라이브 링크 저장
        Live live = null;
        if (request.getLiveLink() != null && !request.getLiveLink().isBlank()) {
            live = Live.builder()
                    .seminar(seminar)
                    .liveUrl(request.getLiveLink())
                    .build();
            liveRepository.save(live);;
        }

        // 세미나 자료
        List<LiveFile> liveFiles = new ArrayList<>();
        if (materials != null) {
            for (MultipartFile file : materials) {
                // S3 업로드
                String url = s3Service.uploadFile(file, "seminar/material");
                // 세미나 자료 저장
                LiveFile liveFile = LiveFile.builder()
                        .seminar(seminar)
                        .fileUrl(url)
                        .fileName(getFileName(file.getOriginalFilename()))
                        .fileExtension(getExtension(file.getOriginalFilename()))
                        .fileSize(file.getSize())
                        .build();
                liveFileRepository.save(liveFile);

                liveFiles.add(liveFile);
            }
        }

        // 연사 정보
        List<Session> sessions = new ArrayList<>();
        for (int i = 0; i < request.getSpeakers().size(); i++) {
            SeminarRegisterRequestDTO.SpeakerRegisterRequest sp = request.getSpeakers().get(i);
            MultipartFile profile = speakerProfiles.get(i);

            // 연사 프로필 이미지 타입 검증
            if (!s3Service.isValidImageFile(profile)) {
                throw new GeneralException(GeneralErrorCode.UNSUPPORTED_FILE_TYPE);
            }

            String profileUrl = s3Service.uploadFile(profile, "seminar/speaker");

            Speaker speaker = Speaker.builder()
                    .name(sp.getName())
                    .organization(sp.getOrganization())
                    .history(sp.getHistory())
                    .profileUrl(profileUrl)
                    .profileFileName(getFileName(profile.getOriginalFilename()))
                    .profileFileExtension(getExtension(profile.getOriginalFilename()))
                    .profileFileSize(profile.getSize())
                    .build();
            speakerRepository.save(speaker);

            Session session = Session.builder()
                    .seminar(seminar)
                    .speaker(speaker)
                    .title(sp.getSessionTitle())
                    .description(sp.getSessionContent())
                    .build();
            sessionRepository.save(session);
            sessions.add(session);
        }

        return SeminarInfoResponseDTO.from(seminar, live, liveFiles, sessions);
    }

    /**
     * 세미나 정보 수정
     *
     * @param seminarId 세미나 ID
     * @param request 세미나 수정 요청 DTO
     * @return 수정된 세미나 정보 DTO
     * @throws GeneralException 세미나 검증, 회차 중복, 기간 검증, 연사/세션 검증 실패 시
     */
    @Transactional
    public SeminarInfoResponseDTO updateSeminar(Long seminarId, SeminarUpdateRequestDTO request) {
        // 세미나 존재 여부 확인
        Seminar seminar = seminarRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

        // 회차 중복 검증
        if (seminarRepository.existsBySeminarNumAndIdNot(request.getSeminarNum(), seminarId)) {
            throw new GeneralException(GeneralErrorCode.SEMINAR_NUM_ALREADY_EXISTS);
        }

        // 세미나 기간 검증
        validatePeriods(
                request.getActiveStartDate(), request.getActiveEndDate(),
                request.getApplyStartDate(), request.getApplyEndDate()
        );

        // 활성화 기간이 겹치는 세미나가 있는지 검증
        if (seminarRepository.overlapsSeminarAndIdNot(seminarId, request.getActiveStartDate(), request.getActiveEndDate())) {
            throw new GeneralException(GeneralErrorCode.SEMINAR_ACTIVE_PERIOD_OVERLAP);
        }

        // 세미나 기본 정보 업데이트
        seminar.updateInfo(
                request.getSeminarNum(),
                request.getSeminarDate(),
                request.getPlace(),
                request.getTopic(),
                request.getActiveStartDate(),
                request.getActiveEndDate(),
                request.getApplyStartDate(),
                request.getApplyEndDate()
        );

        // 라이브 링크 처리
        if (request.getLiveLink() == null || request.getLiveLink().isBlank()) {
            seminar.updateLive(null);
        } else {
            // 값이 있으면 등록/수정
            Live live = liveRepository.findBySeminar(seminar)
                    .orElse(Live.builder().seminar(seminar).build());
            live.updateUrl(request.getLiveLink());
            liveRepository.save(live);
        }


        // 연사 정보 업데이트
        for (SeminarUpdateRequestDTO.SpeakerUpdateRequest spReq : request.getSpeakers()) {
            Speaker speaker = speakerRepository.findById(spReq.getSpeakerId())
                    .orElseThrow(() -> new GeneralException(GeneralErrorCode.SPEAKER_NOT_FOUND));

            Session session = sessionRepository.findBySeminarAndSpeaker(seminar, speaker)
                    .orElseThrow(() -> new GeneralException(GeneralErrorCode.SESSION_NOT_FOUND));

            speaker.updateInfo(spReq.getName(), spReq.getOrganization(), spReq.getHistory());
            session.updateInfo(spReq.getSessionTitle(), spReq.getSessionContent());
        }

        // 응답 DTO 생성
        Live live = liveRepository.findBySeminar(seminar).orElse(null);
        List<LiveFile> liveFiles = liveFileRepository.findBySeminar(seminar);
        List<Session> sessions = sessionRepository.findSessionsBySeminar(seminar);

        return SeminarInfoResponseDTO.from(seminar, live, liveFiles, sessions);
    }

    /**
     * 세미나 파일(썸네일, 자료, 연사 프로필) 수정
     *
     * @param seminarId 세미나 ID
     * @param thumbnailFile 교체할 썸네일 파일
     * @param materials 추가할 세미나 자료 파일 리스트
     * @param deleteMaterialUrls 삭제할 세미나 자료 URL 리스트
     * @param speakerIds 교체할 연사 ID 리스트
     * @param speakerProfiles 교체할 연사 프로필 파일 리스트
     * @return 수정된 세미나 정보 DTO
     * @throws GeneralException 세미나 검증, 파일 검증, 연사 검증 실패 시
     */
    @Transactional
    public SeminarInfoResponseDTO updateSeminarFiles(
            Long seminarId,
            MultipartFile thumbnailFile,
            List<MultipartFile> materials,
            List<String> deleteMaterialUrls,
            List<Long> speakerIds,
            List<MultipartFile> speakerProfiles
    ) {
        // 세미나 존재 여부 확인
        Seminar seminar = seminarRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

        // 썸네일 교체
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            if (!s3Service.isValidImageFile(thumbnailFile)) {
                throw new GeneralException(GeneralErrorCode.UNSUPPORTED_FILE_TYPE);
            }
            // 기존 파일 삭제
            if (seminar.getThumbnailUrl() != null) {
                String oldKey = s3Service.extractS3KeyFromUrl(seminar.getThumbnailUrl());
                if (oldKey != null) s3Service.deleteFile(oldKey);
            }
            // 새 파일 업로드
            String newUrl = s3Service.uploadFile(thumbnailFile, "seminar/thumbnail");
            seminar.updateThumbnail(
                    newUrl,
                    getFileName(thumbnailFile.getOriginalFilename()),
                    getExtension(thumbnailFile.getOriginalFilename()),
                    thumbnailFile.getSize()
            );
        }

        // 자료 파일 삭제
        if (deleteMaterialUrls != null) {
            for (String url : deleteMaterialUrls) {
                LiveFile liveFile = liveFileRepository.findByFileUrl(url)
                        .orElseThrow(() -> new GeneralException(GeneralErrorCode.LIVE_FILE_NOT_FOUND));
                String s3Key = s3Service.extractS3KeyFromUrl(liveFile.getFileUrl());
                if (s3Key != null) s3Service.deleteFile(s3Key);
                liveFileRepository.delete(liveFile);
            }
        }

        // 자료 파일 추가
        if (materials != null) {
            for (MultipartFile file : materials) {
                String url = s3Service.uploadFile(file, "seminar/material");
                LiveFile newLiveFile = LiveFile.builder()
                        .seminar(seminar)
                        .fileUrl(url)
                        .fileName(getFileName(file.getOriginalFilename()))
                        .fileExtension(getExtension(file.getOriginalFilename()))
                        .fileSize(file.getSize())
                        .build();
                liveFileRepository.save(newLiveFile);
            }
        }

        // 연사 프로필 사진 교체
        if (speakerIds != null && speakerProfiles != null && speakerIds.size() == speakerProfiles.size()) {
            for (int i = 0; i < speakerIds.size(); i++) {
                Long speakerId = speakerIds.get(i);
                MultipartFile newProfile = speakerProfiles.get(i);

                Speaker speaker = speakerRepository.findById(speakerId)
                        .orElseThrow(() -> new GeneralException(GeneralErrorCode.SPEAKER_NOT_FOUND));

                // 기존 프로필 삭제
                if (speaker.getProfileUrl() != null) {
                    String oldKey = s3Service.extractS3KeyFromUrl(speaker.getProfileUrl());
                    if (oldKey != null) s3Service.deleteFile(oldKey);
                }

                // 새 프로필 업로드
                if (!s3Service.isValidImageFile(newProfile)) {
                    throw new GeneralException(GeneralErrorCode.UNSUPPORTED_FILE_TYPE);
                }
                String newUrl = s3Service.uploadFile(newProfile, "seminar/speaker");

                speaker.updateProfile(
                        newUrl,
                        getFileName(newProfile.getOriginalFilename()),
                        getExtension(newProfile.getOriginalFilename()),
                        newProfile.getSize()
                );
                speakerRepository.save(speaker);
            }
        }

        // 응답 DTO 생성
        Live live = liveRepository.findBySeminar(seminar).orElse(null);
        List<LiveFile> liveFiles = liveFileRepository.findBySeminar(seminar);
        List<Session> sessions = sessionRepository.findSessionsBySeminar(seminar);

        return SeminarInfoResponseDTO.from(seminar, live, liveFiles, sessions);
    }

    /**
     * 세미나 영구 삭제
     *
     * @param seminarId 삭제할 세미나의 ID
     * @throws GeneralException 세미나가 존재하지 않는 경우
     */
    @Transactional
    public void deleteSeminar(Long seminarId) {
        // 세미나 존재 여부 확인
        Seminar seminar = seminarRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

        // 세미나 썸네일, 연사 프로필, 세미나 자료 S3에서 삭제
        if (seminar.getThumbnailUrl() != null) {
            String key = s3Service.extractS3KeyFromUrl(seminar.getThumbnailUrl());
            s3Service.deleteFile(key);
        }

        for (LiveFile lf : seminar.getLiveFiles()) {
            String key = s3Service.extractS3KeyFromUrl(lf.getFileUrl());
            s3Service.deleteFile(key);
        }

        for (Session session : seminar.getSessions()) {
            Speaker speaker = session.getSpeaker();
            if (speaker.getProfileUrl() != null) {
                String key = s3Service.extractS3KeyFromUrl(speaker.getProfileUrl());
                s3Service.deleteFile(key);
            }
        }

        seminarRepository.delete(seminar);
    }

    // 세미나 기간 검증
    // 시작일은 종료일 보다 항상 먼저 + 세미나 신청 기간은 세미나 활성화 기간 안에 포함되어야 함
    private void validatePeriods(LocalDateTime activeStart, LocalDateTime activeEnd,
                                 LocalDateTime applyStart, LocalDateTime applyEnd) {
        if (!activeStart.isBefore(activeEnd) || !applyStart.isBefore(applyEnd)) {
            throw new GeneralException(GeneralErrorCode.INVALID_PERIOD_ORDER);
        }
        if (applyStart.isBefore(activeStart) || applyEnd.isAfter(activeEnd)) {
            throw new GeneralException(GeneralErrorCode.INVALID_SEMINAR_PERIOD);
        }
    }

    // 연사 수와 프로필 이미지 수 일치하는지 검증
    private void validateSpeakersAndProfiles(int speakerCount, List<MultipartFile> speakerProfiles) {
        if (speakerCount != speakerProfiles.size()) {
            throw new GeneralException(GeneralErrorCode.INVALID_SPEAKER_PROFILE_COUNT);
        }
    }

    // 파일 확장자 추출
    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return null;
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    // 파일 확장자를 제외한 이름만 추출
    private String getFileName(String fileName) {
        if (fileName == null) return null;
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }
}