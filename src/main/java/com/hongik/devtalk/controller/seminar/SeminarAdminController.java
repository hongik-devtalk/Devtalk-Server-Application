package com.hongik.devtalk.controller.seminar;

import com.hongik.devtalk.domain.seminar.admin.dto.ApplicantResponseDTO;
import com.hongik.devtalk.domain.seminar.admin.dto.QuestionResponseDTO;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarCardResponseDTO;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarReviewResponseDTO;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.seminar.SeminarAdminCommandService;
import com.hongik.devtalk.service.seminar.SeminarAdminQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

        return ApiResponse.onSuccess("후기를 홈 화면에서 제외했습니다.");
    }

    @Operation(summary = "세미나 후기 삭제", description = "세미나 후기를 영구적으로 삭제합니다.")
    @DeleteMapping("/reviews/{reviewId}")
    public ApiResponse<Void> deleteReview(
            @Parameter(description = "후기 ID", required = true)
            @PathVariable Long reviewId
    ) {

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