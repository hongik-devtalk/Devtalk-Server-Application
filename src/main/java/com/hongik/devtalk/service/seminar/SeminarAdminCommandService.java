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
import com.hongik.devtalk.repository.review.ReviewRepository;
import com.hongik.devtalk.repository.seminar.LiveFileRepository;
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

    private final ReviewRepository reviewRepository;
    private final SeminarRepository seminarRepository;
    private final SpeakerRepository speakerRepository;
    private final SessionRepository sessionRepository;
    private final LiveFileRepository liveFileRepository;
    private final LiveRepository liveRepository;
    private final S3Service s3Service;

    /**
     * 후기 ID로 특정 후기를 홈 화면에 노출
     *
     * @param reviewId 후기 ID
     * @throws GeneralException 후기가 존재하지 않을 경우
     */
    @Transactional
    public void exposeReviewToHome(Long reviewId) {
        // 후기 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.REVIEW_NOT_FOUND));

        review.updateIsNote(true);
    }

    /**
     * 후기 ID로 특정 후기를 홈 화면에서 숨김
     *
     * @param reviewId 후기 ID
     * @throws GeneralException 후기가 존재하지 않을 경우
     */
    @Transactional
    public void hideReviewFromHome(Long reviewId) {
        // 후기 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.REVIEW_NOT_FOUND));

        review.updateIsNote(false);
    }

    /**
     * 후기 ID로 특정 후기를 영구 삭제
     *
     * @param reviewId 후기 ID
     * @throws GeneralException 후기가 존재하지 않을 경우
     */
    @Transactional
    public void deleteReview(Long reviewId) {
        // 후기 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.REVIEW_NOT_FOUND));

        reviewRepository.delete(review);
    }

    /**
     * 세미나 등록
     *
     * @param request 세미나 등록 요청 DTO
     * @param thumbnailFile 세미나 대표 썸네일
     * @param materials 세미나 자료 파일 리스트
     * @param speakerProfiles 연사 프로필 이미지 리스트
     * @return 등록된 세미나 정보 DTO
     * @throws GeneralException 기간 검증, 파일 검증 실패 시
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
        validateSpeakersAndProfiles(request.getSpeakers().size(), speakerProfiles);

        // 세미나 정보
        // 이미지 타입 확인
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
                .status(SeminarStatus.UPCOMING)
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