package com.hongik.devtalk.controller.seminar;

import com.hongik.devtalk.domain.seminar.admin.dto.*;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.seminar.SeminarAdminCommandService;
import com.hongik.devtalk.service.seminar.SeminarAdminQueryService;
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

    @Operation(summary = "세미나 카드리스트 조회", description = "세미나 카드리스트를 조회합니다.")
    @GetMapping("/card")
    public ApiResponse<List<SeminarCardResponseDTO>> getSeminarCards() {

        return ApiResponse.onSuccess("세미나 카드 리스트 조회에 성공했습니다.");
    }

    @Operation(summary = "세미나 등록", description = "세미나 기본 정보와 파일을 함께 등록합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<SeminarInfoResponseDTO> registerSeminar(
            @Valid @RequestPart("seminarRequest") SeminarRegisterRequestDTO request,
            @RequestPart("thumbnailFile") MultipartFile thumbnailFile,
            @RequestPart(value = "materials", required = false) List<MultipartFile> materials,
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

    @Operation(summary = "세미나 삭제", description = "해당 세미나를 영구적으로 삭제합니다.")
    @DeleteMapping("/{seminarId}")
    public ApiResponse<Void> deleteSeminar(
            @Parameter(description = "삭제할 세미나 ID", required = true)
            @PathVariable Long seminarId
    ) {

        return ApiResponse.onSuccess("세미나를 삭제했습니다.");
    }

    @Operation(summary = "세미나 후기 목록 조회", description = "세미나 상세 정보 조회 페이지 - 후기 목록 부분을 조회합니다.")
    @GetMapping("/{seminarId}/reviews")
    public ApiResponse<List<SeminarReviewResponseDTO>> getSeminarReviews(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId
    ) {

        return ApiResponse.onSuccess("세미나 후기 리스트 조회에 성공했습니다.");
    }

    @Operation(summary = "세미나 후기 홈 노출 ON", description = "세미나 후기를 홈 화면에 노출되게 설정합니다.")
    @PostMapping("/reviews/{reviewId}/home/on")
    public ApiResponse<Void> exposeReviewOnHome(
            @Parameter(description = "후기 ID", required = true)
            @PathVariable Long reviewId
    ) {
        seminarAdminCommandService.exposeReviewToHome(reviewId);
        return ApiResponse.onSuccess("후기를 홈 화면에 등록했습니다.");
    }

    @Operation(summary = "세미나 후기 홈 노출 OFF", description = "세미나 후기를 홈 화면에서 제외합니다.")
    @PostMapping("/reviews/{reviewId}/home/off")
    public ApiResponse<Void> removeReviewFromHome(
            @Parameter(description = "후기 ID", required = true)
            @PathVariable Long reviewId
    ) {
        seminarAdminCommandService.hideReviewFromHome(reviewId);
        return ApiResponse.onSuccess("후기를 홈 화면에서 제외했습니다.");
    }

    @Operation(summary = "세미나 후기 삭제", description = "세미나 후기를 영구적으로 삭제합니다.")
    @DeleteMapping("/reviews/{reviewId}")
    public ApiResponse<Void> deleteReview(
            @Parameter(description = "후기 ID", required = true)
            @PathVariable Long reviewId
    ) {
        seminarAdminCommandService.deleteReview(reviewId);
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
}