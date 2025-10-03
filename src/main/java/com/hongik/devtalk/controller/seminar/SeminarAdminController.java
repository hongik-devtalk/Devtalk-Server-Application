package com.hongik.devtalk.controller.seminar;

import com.hongik.devtalk.domain.seminar.admin.dto.*;
import com.hongik.devtalk.domain.seminar.dto.SeminarListDto;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.seminar.SeminarAdminCommandService;
import com.hongik.devtalk.service.seminar.SeminarAdminQueryService;
import com.hongik.devtalk.service.seminar.SeminarListService;
import com.hongik.devtalk.service.seminar.SeminarReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "[Admin]Seminar", description = "어드민 - 세미나 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/seminars")
public class SeminarAdminController {

    private final SeminarAdminQueryService seminarAdminQueryService;
    private final SeminarAdminCommandService seminarAdminCommandService;
    private final SeminarReviewService seminarReviewService;
    private final SeminarListService seminarListService;

    @Operation(summary = "세미나 카드리스트 조회", description = "세미나 카드리스트를 조회합니다.")
    @GetMapping("/card")
    public ApiResponse<SeminarListDto.SeminarResDtoList> getSeminarCards() {
        SeminarListDto.SeminarResDtoList result = seminarListService.seminarList();
        return ApiResponse.onSuccess("세미나 카드 리스트 조회에 성공했습니다.", result);
    }

    @Operation(summary = "세미나 상세 조회", description = "등록된 세미나의 상세 정보를 조회합니다.")
    @GetMapping("/{seminarId}")
    public ApiResponse<SeminarInfoResponseDTO> getSeminarInfo(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId
    ) {
        SeminarInfoResponseDTO result = seminarAdminQueryService.getSeminarInfo(seminarId);
        return ApiResponse.onSuccess("세미나 상세 조회에 성공했습니다.", result);
    }

    @Operation(summary = "세미나 등록", description = "세미나 기본 정보와 파일을 함께 등록합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<SeminarInfoResponseDTO> registerSeminar(
            @Parameter(description = "세미나 등록 요청 DTO (회차, 주제, 장소, 일정, 연사 정보 등 기본 정보)")
            @Valid @RequestPart("seminarRequest") SeminarRegisterRequestDTO request,

            @Parameter(description = "세미나 대표 썸네일 이미지 파일 (필수)")
            @RequestPart("thumbnailFile") MultipartFile thumbnailFile,

            @Parameter(description = "세미나 자료 파일 리스트 (선택)")
            @RequestPart(value = "materials", required = false) List<MultipartFile> materials,

            @Parameter(description = "연사 프로필 이미지 파일 리스트 (연사 수와 동일 개수 필수)")
            @RequestPart("speakerProfiles") List<MultipartFile> speakerProfiles
    ) {
        SeminarInfoResponseDTO result =
                seminarAdminCommandService.registerSeminar(request, thumbnailFile, materials, speakerProfiles);
        return ApiResponse.onSuccess("세미나 등록에 성공했습니다.", result);
    }

    @Operation(summary = "세미나 정보 수정", description = "세미나 기본 정보, 연사 정보, 라이브 링크 등을 수정합니다. " +
            "PUT 메서드로, 수정하지 않는 필드까지 모두 보내주셔야 합니다. 라이브 링크를 삭제하는 경우에는 null로 보내주세요.")
    @PutMapping("/{seminarId}")
    public ApiResponse<SeminarInfoResponseDTO> updateSeminar(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId,
            @Valid @RequestBody SeminarUpdateRequestDTO request
    ) {
        SeminarInfoResponseDTO response = seminarAdminCommandService.updateSeminar(seminarId, request);
        return ApiResponse.onSuccess("세미나 정보 수정에 성공했습니다.", response);
    }

    @Operation(summary = "세미나 파일 수정", description = "세미나 썸네일, 자료 파일 추가/삭제, 연사 프로필을 수정합니다.")
    @PatchMapping(value = "/{seminarId}/files", consumes = {"multipart/form-data"})
    public ApiResponse<SeminarInfoResponseDTO> updateSeminarFiles(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId,

            @Parameter(description = "새로 교체할 세미나 대표 썸네일 이미지 (있으면 기존 썸네일 교체)")
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,

            @Parameter(description = "추가할 세미나 자료 파일 리스트")
            @RequestPart(value = "materials", required = false) List<MultipartFile> materials,

            @Parameter(description = "삭제할 세미나 자료 파일 URL 리스트")
            @RequestParam(value = "deleteMaterialUrls", required = false) List<String> deleteMaterialUrls,

            @Parameter(description = "프로필 이미지를 교체할 연사 ID 리스트 (speakerProfiles와 인덱스 매칭)")
            @RequestParam(value = "speakerIds", required = false) List<Long> speakerIds,

            @Parameter(description = "새로 교체할 연사 프로필 이미지 파일 리스트 (speakerIds와 인덱스 매칭)")
            @RequestPart(value = "speakerProfiles", required = false) List<MultipartFile> speakerProfiles
    ) {
        SeminarInfoResponseDTO result = seminarAdminCommandService.updateSeminarFiles(
                seminarId, thumbnailFile, materials, deleteMaterialUrls, speakerIds, speakerProfiles
        );
        return ApiResponse.onSuccess("세미나 파일 수정에 성공했습니다.", result);
    }

    @Operation(summary = "세미나 삭제", description = "해당 세미나를 영구적으로 삭제합니다.")
    @DeleteMapping("/{seminarId}")
    public ApiResponse<Void> deleteSeminar(
            @Parameter(description = "삭제할 세미나 ID", required = true)
            @PathVariable Long seminarId
    ) {
        seminarAdminCommandService.deleteSeminar(seminarId);
        return ApiResponse.onSuccess("세미나를 삭제했습니다.");
    }

    @Operation(summary = "세미나 후기 목록 조회", description = "세미나 상세 정보 조회 페이지 - 후기 목록 부분을 조회합니다.")
    @GetMapping("/{seminarId}/reviews")
    public ApiResponse<List<SeminarReviewResponseDTO>> getSeminarReviews(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId
    ) {
        List<SeminarReviewResponseDTO> result = seminarReviewService.getSeminarReviews(seminarId);
        return ApiResponse.onSuccess("세미나 후기 리스트 조회에 성공했습니다.", result);
    }

    @Operation(summary = "세미나 후기 홈 노출 ON", description = "세미나 후기를 홈 화면에 노출되게 설정합니다.")
    @PostMapping("/reviews/{reviewId}/home/on")
    public ApiResponse<Void> exposeReviewOnHome(
            @Parameter(description = "후기 ID", required = true)
            @PathVariable Long reviewId
    ) {
        seminarReviewService.exposeReviewToHome(reviewId);
        return ApiResponse.onSuccess("후기를 홈 화면에 등록했습니다.");
    }

    @Operation(summary = "세미나 후기 홈 노출 OFF", description = "세미나 후기를 홈 화면에서 제외합니다.")
    @PostMapping("/reviews/{reviewId}/home/off")
    public ApiResponse<Void> removeReviewFromHome(
            @Parameter(description = "후기 ID", required = true)
            @PathVariable Long reviewId
    ) {
        seminarReviewService.hideReviewFromHome(reviewId);
        return ApiResponse.onSuccess("후기를 홈 화면에서 제외했습니다.");
    }

    @Operation(summary = "세미나 후기 삭제", description = "세미나 후기를 영구적으로 삭제합니다.")
    @DeleteMapping("/reviews/{reviewId}")
    public ApiResponse<Void> deleteReview(
            @Parameter(description = "후기 ID", required = true)
            @PathVariable Long reviewId
    ) {
        seminarReviewService.deleteReview(reviewId);
        return ApiResponse.onSuccess("세미나 후기를 삭제했습니다.");
    }

    @Operation(summary = "세미나 신청자 정보 조회", description = "세미나 신청자 정보를 조회합니다.")
    @GetMapping("/{seminarId}/applicants")
    public ApiResponse<List<ApplicantResponseDTO>> getApplicantsInfo(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId
    ) {
        List<ApplicantResponseDTO> result = seminarAdminQueryService.getApplicants(seminarId);
        return ApiResponse.onSuccess("세미나 신청자 조회에 성공했습니다.", result);
    }

    @Operation(summary = "세미나 연사별 질문 조회", description = "세미나 연사별 질문 정보를 조회합니다.")
    @GetMapping("/{seminarId}/questions")
    public ApiResponse<QuestionResponseDTO> getSpeakerQuestions(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId
    ) {
        QuestionResponseDTO result = seminarAdminQueryService.getQuestions(seminarId);
        return ApiResponse.onSuccess("세미나 연사별 질문 조회에 성공했습니다.", result);
    }

    @Operation(summary = "세미나 회차 리스트 조회", description = "현재 등록된 세미나의 회차 번호만 리스트로 조회합니다.")
    @GetMapping("/nums")
    public ApiResponse<SeminarNumResponseDTO> getSeminarNums() {
        SeminarNumResponseDTO result = seminarAdminQueryService.getSeminarNums();
        return ApiResponse.onSuccess("세미나 회차 리스트 조회에 성공했습니다.", result);
    }
}