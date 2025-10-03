package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.*;
import com.hongik.devtalk.domain.enums.SeminarStatus;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarRegisterRequestDTO;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarInfoResponseDTO;
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
        validatePeriods(request);
        validateSpeakersAndProfiles(request, speakerProfiles);

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
        if (request.getLiveLink() != null && !request.getLiveLink().isBlank()) {
            Live live = Live.builder()
                    .seminar(seminar)
                    .liveUrl(request.getLiveLink())
                    .build();
            liveRepository.save(live);
        }

        // 세미나 자료
        List<SeminarInfoResponseDTO.FileInfo> materialInfos = new ArrayList<>();
        if (materials != null) {
            for (MultipartFile file : materials) {
                // S3 업로드
                String url = s3Service.uploadFile(file, "seminar/material");
                // 세미나 자료 저장
                LiveFile livefile = LiveFile.builder()
                        .seminar(seminar)
                        .fileUrl(url)
                        .fileName(getFileName(file.getOriginalFilename()))
                        .fileExtension(getExtension(file.getOriginalFilename()))
                        .fileSize(file.getSize())
                        .build();
                liveFileRepository.save(livefile);

                materialInfos.add(toFileInfo(file, url));
            }
        }

        // 연사 + 세션 정보
        // 이미지 타입 확인
        for (MultipartFile profile : speakerProfiles) {
            if (!s3Service.isValidImageFile(profile)) {
                throw new GeneralException(GeneralErrorCode.UNSUPPORTED_FILE_TYPE);
            }
        }

        List<SeminarInfoResponseDTO.SpeakerResponse> speakerInfos = new ArrayList<>();
        for (int i = 0; i < request.getSpeakers().size(); i++) {
            SeminarRegisterRequestDTO.SpeakerRegisterRequest sp = request.getSpeakers().get(i);
            MultipartFile profile = speakerProfiles.get(i);

            // S3 업로드
            String profileUrl = s3Service.uploadFile(profile, "seminar/speaker");

            // 연사 + 세션 저장
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

            speakerInfos.add(
                    SeminarInfoResponseDTO.SpeakerResponse.builder()
                            .speakerId(speaker.getId())
                            .name(speaker.getName())
                            .organization(speaker.getOrganization())
                            .history(speaker.getHistory())
                            .sessionTitle(session.getTitle())
                            .sessionContent(session.getDescription())
                            .profile(toFileInfo(profile, profileUrl))
                            .build()
            );
        }

        return SeminarInfoResponseDTO.builder()
                .seminarId(seminar.getId())
                .seminarNum(seminar.getSeminarNum())
                .topic(seminar.getTopic())
                .seminarDate(seminar.getSeminarDate())
                .place(seminar.getPlace())
                .liveLink(request.getLiveLink())
                .activeStartDate(seminar.getActiveStartDate())
                .activeEndDate(seminar.getActiveEndDate())
                .applyStartDate(seminar.getStartDate())
                .applyEndDate(seminar.getEndDate())
                .thumbnail(toFileInfo(thumbnailFile, thumbnailUrl))
                .materials(materialInfos)
                .speakers(speakerInfos)
                .build();
    }

    // 세미나 기간 검증
    private void validatePeriods(SeminarRegisterRequestDTO req) {
        if (!req.getActiveStartDate().isBefore(req.getActiveEndDate())
                || !req.getApplyStartDate().isBefore(req.getApplyEndDate())) {
            throw new GeneralException(GeneralErrorCode.INVALID_PERIOD_ORDER);
        }

        if (req.getApplyStartDate().isBefore(req.getActiveStartDate())
                || req.getApplyEndDate().isAfter(req.getActiveEndDate())) {
            throw new GeneralException(GeneralErrorCode.INVALID_SEMINAR_PERIOD);
        }
    }

    // 연사 수와 프로필 이미지 수가 일치하는지 검증
    private void validateSpeakersAndProfiles(SeminarRegisterRequestDTO req, List<MultipartFile> speakerProfiles) {
        if (req.getSpeakers().size() != speakerProfiles.size()) {
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

    private SeminarInfoResponseDTO.FileInfo toFileInfo(MultipartFile file, String url) {
        return SeminarInfoResponseDTO.FileInfo.builder()
                .fileName(getFileName(file.getOriginalFilename()))
                .fileExtension(getExtension(file.getOriginalFilename()))
                .fileSize(file.getSize())
                .fileUrl(url)
                .build();
    }
}