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
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON2000", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SERVER_5001", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/card")
    public ApiResponse<SeminarListDto.SeminarResDtoList> getSeminarCards() {
        SeminarListDto.SeminarResDtoList result = seminarListService.seminarList();
        return ApiResponse.onSuccess("세미나 카드 리스트 조회에 성공했습니다.", result);
    }

    @Operation(summary = "세미나 상세 조회", description = "등록된 세미나의 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON2000", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINARINFO_4041", description = "세미나 ID를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SERVER_5001", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/{seminarId}")
    public ApiResponse<SeminarInfoResponseDTO> getSeminarInfo(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId
    ) {
        SeminarInfoResponseDTO result = seminarAdminQueryService.getSeminarInfo(seminarId);
        return ApiResponse.onSuccess("세미나 상세 조회에 성공했습니다.", result);
    }

    @Operation(summary = "세미나 등록", description = "세미나 기본 정보와 파일을 함께 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON2000", description = "등록 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINAR_4001",description = "세미나 신청 기간이 활성화 기간을 벗어남",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINAR_4002", description = "연사 정보 수와 프로필 파일 수 불일치",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINAR_4003", description = "시작일은 종료일보다 빨라야 함",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINAR_4004", description = "이미 존재하는 회차",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FILE_4151", description = "지원하지 않는 이미지 형식",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_5001", description = "S3 업로드 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_5003", description = "S3 연결 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SERVER_5001", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<SeminarInfoResponseDTO> registerSeminar(
            @Parameter(description = "세미나 등록 요청 DTO (회차, 주제, 장소, 일정, 연사 정보 등 기본 정보)")
            @Valid @RequestPart("seminarRequest") SeminarRegisterRequestDTO request,

            @Parameter(description = "세미나 대표 썸네일 이미지 파일 (필수)")
            @RequestPart("thumbnailFile") MultipartFile thumbnailFile,

            @Parameter(description = "세미나 자료 파일 리스트 (선택)")
            @RequestPart(value = "materials", required = false) List<MultipartFile> materials,

            @Parameter(description = "연사 프로필 이미지 파일 리스트 (연사 수와 동일 개수 필수)<br>" +
                    "연사 정보 순서와 동일한 순서로 업로드 해야 합니다.<br>" +
                    "예: 첫 번째 연사 정보 → speakerProfiles 배열의 첫 번째 프로필 이미지와 매핑")
            @RequestPart("speakerProfiles") List<MultipartFile> speakerProfiles
    ) {
        SeminarInfoResponseDTO result =
                seminarAdminCommandService.registerSeminar(request, thumbnailFile, materials, speakerProfiles);
        return ApiResponse.onSuccess("세미나 등록에 성공했습니다.", result);
    }

    @Operation(summary = "세미나 정보 수정", description = "세미나 기본 정보, 연사 정보, 라이브 링크 등을 수정합니다. 세미나 파일 수정은 /{seminarId}/files API 사용해주세요.<br>" +
            "PUT 메서드로, 수정하지 않는 필드까지 모두 보내주셔야 합니다. 등록된 라이브 링크를 삭제할때는 liveLink를 null로 보내주세요.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON2000", description = "수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINARINFO_4041", description = "세미나 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINAR_4001", description = "세미나 신청 기간이 활성화 기간을 벗어남",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINAR_4003", description = "시작일은 종료일보다 빨라야 함",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINAR_4004", description = "이미 존재하는 회차",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SPEAKER_4041", description = "연사 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SESSION_4041", description = "세션 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SERVER_5001", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PutMapping("/{seminarId}")
    public ApiResponse<SeminarInfoResponseDTO> updateSeminar(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId,
            @Valid @RequestBody SeminarUpdateRequestDTO request
    ) {
        SeminarInfoResponseDTO response = seminarAdminCommandService.updateSeminar(seminarId, request);
        return ApiResponse.onSuccess("세미나 정보 수정에 성공했습니다.", response);
    }

    @Operation(summary = "세미나 파일 수정", description = "세미나 썸네일, 자료 파일 추가/삭제, 연사 프로필 사진을 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON2000", description = "수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINARINFO_4041", description = "세미나 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SPEAKER_4041", description = "연사 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SESSION_4041", description = "세션 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FILE_4151", description = "지원하지 않는 이미지 형식",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_5001", description = "S3 업로드 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SERVER_5001", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PatchMapping(value = "/{seminarId}/files", consumes = {"multipart/form-data"})
    public ApiResponse<SeminarInfoResponseDTO> updateSeminarFiles(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId,

            @Parameter(description = "새로 교체할 세미나 대표 썸네일 이미지 (있으면 기존 썸네일 교체)")
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile,

            @Parameter(description = "추가할 세미나 자료 파일 리스트")
            @RequestPart(value = "materials", required = false) List<MultipartFile> materials,

            @Parameter(description = "삭제할 세미나 자료 URL 리스트")
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

    @Operation(summary = "세미나 삭제", description = "해당 세미나를 영구적으로 삭제합니다. 연관된 세미나 후기, 신청 정보도 같이 삭제됩니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON2000", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINARINFO_4041", description = "세미나 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "S3_5002", description = "S3 삭제 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SERVER_5001", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @DeleteMapping("/{seminarId}")
    public ApiResponse<Void> deleteSeminar(
            @Parameter(description = "삭제할 세미나 ID", required = true)
            @PathVariable Long seminarId
    ) {
        seminarAdminCommandService.deleteSeminar(seminarId);
        return ApiResponse.onSuccess("세미나를 삭제했습니다.");
    }

    @Operation(summary = "세미나 후기 목록 조회", description = "세미나 상세 정보 조회 페이지 - 후기 목록 부분을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON2000", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINARINFO_4041", description = "세미나 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SERVER_5001", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/{seminarId}/reviews")
    public ApiResponse<List<SeminarReviewResponseDTO>> getSeminarReviews(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId
    ) {
        List<SeminarReviewResponseDTO> result = seminarReviewService.getSeminarReviews(seminarId);
        return ApiResponse.onSuccess("세미나 후기 리스트 조회에 성공했습니다.", result);
    }

    @Operation(summary = "세미나 후기 홈 노출 ON", description = "세미나 후기를 홈 화면에 노출되게 설정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON2000", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW_4041", description = "후기 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SERVER_5001", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/reviews/{reviewId}/home/on")
    public ApiResponse<Void> exposeReviewOnHome(
            @Parameter(description = "후기 ID", required = true)
            @PathVariable Long reviewId
    ) {
        seminarReviewService.exposeReviewToHome(reviewId);
        return ApiResponse.onSuccess("후기를 홈 화면에 등록했습니다.");
    }

    @Operation(summary = "세미나 후기 홈 노출 OFF", description = "세미나 후기를 홈 화면에서 제외합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON2000", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW_4041", description = "후기 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SERVER_5001", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/reviews/{reviewId}/home/off")
    public ApiResponse<Void> removeReviewFromHome(
            @Parameter(description = "후기 ID", required = true)
            @PathVariable Long reviewId
    ) {
        seminarReviewService.hideReviewFromHome(reviewId);
        return ApiResponse.onSuccess("후기를 홈 화면에서 제외했습니다.");
    }

    @Operation(summary = "세미나 후기 삭제", description = "세미나 후기를 영구적으로 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON2000", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW_4041", description = "후기 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SERVER_5001", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @DeleteMapping("/reviews/{reviewId}")
    public ApiResponse<Void> deleteReview(
            @Parameter(description = "후기 ID", required = true)
            @PathVariable Long reviewId
    ) {
        seminarReviewService.deleteReview(reviewId);
        return ApiResponse.onSuccess("세미나 후기를 삭제했습니다.");
    }

    @Operation(summary = "세미나 신청자 정보 조회", description = "세미나 신청자 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON2000", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINARINFO_4041", description = "세미나 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SERVER_5001", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/{seminarId}/applicants")
    public ApiResponse<List<ApplicantResponseDTO>> getApplicantsInfo(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId
    ) {
        List<ApplicantResponseDTO> result = seminarAdminQueryService.getApplicants(seminarId);
        return ApiResponse.onSuccess("세미나 신청자 조회에 성공했습니다.", result);
    }

    @Operation(summary = "세미나 연사별 질문 조회", description = "세미나 연사별 질문 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON2000", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEMINARINFO_4041", description = "세미나 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SERVER_5001", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/{seminarId}/questions")
    public ApiResponse<QuestionResponseDTO> getSpeakerQuestions(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId
    ) {
        QuestionResponseDTO result = seminarAdminQueryService.getQuestions(seminarId);
        return ApiResponse.onSuccess("세미나 연사별 질문 조회에 성공했습니다.", result);
    }

    @Operation(summary = "세미나 회차 리스트 조회", description = "세미나 신청자 관리 페이지에서 현재 등록된 세미나의 회차 번호만 리스트로 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON2000", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SERVER_5001", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/nums")
    public ApiResponse<SeminarNumResponseDTO> getSeminarNums() {
        SeminarNumResponseDTO result = seminarAdminQueryService.getSeminarNums();
        return ApiResponse.onSuccess("세미나 회차 리스트 조회에 성공했습니다.", result);
    }
}