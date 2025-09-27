package com.hongik.devtalk.controller.mainpage;

import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.domain.mainpage.dto.*;
import com.hongik.devtalk.domain.enums.ImageType;
import com.hongik.devtalk.domain.mainpage.MainpageImagesResponseDto;
import com.hongik.devtalk.service.mainpage.MainpageImagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "Mainpage", description = "홈페이지 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/home")
public class MainpageController {

    private final MainpageImagesService mainpageImagesService;

    // Images APIs
    @GetMapping("/images")
    @Operation(
            summary = "홍보 사진 현재 상태 조회",
            description = "Devtalk 소개 사진과 이전 세미나 보러가기 사진(각 1장)의 현재 상태를 조회합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "홍보 사진 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ApiResponse<MainpageImagesResponseDto> getMainpageImages(
//            @Parameter(description = "인증 토큰", required = true)
//            @RequestHeader("Authorization") String authorization
    ) {
        MainpageImagesResponseDto result = mainpageImagesService.getMainpageImages();
        return ApiResponse.onSuccess("홍보 사진 정보를 조회했습니다.", result);
    }

    @PostMapping("/images")
    @Operation(
            summary = "홍보 사진 업로드/교체",
            description = "홍보 사진 업로드/교체 (1장 제한, 업로드 시 기존 이미지 자동 교체)"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "이미지 업로드 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (파일 형식 또는 크기 오류)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "413",
                    description = "파일 크기 초과 (최대 10MB)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "415",
                    description = "지원하지 않는 미디어 타입 (JPEG, PNG, WebP만 허용)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ApiResponse<ImageInfoDto> uploadMainpageImage(
//            @Parameter(description = "인증 토큰", required = true)
//            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "이미지 종류 (INTRO 또는 PREVIOUS_SEMINAR)", required = true)
            @RequestParam("type") ImageType type,
            @Parameter(description = "업로드할 이미지 파일 (최대 10MB, 허용 MIME: image/jpeg, image/png, image/webp)", required = true)
            @RequestParam("file") MultipartFile file
    ) {
        // TODO: S3 이미지 업로드/교체 로직 구현
        // TODO: 파일 유효성 검사 (크기, MIME 타입)
        return ApiResponse.onSuccess(type.getDescription() + " 이미지를 저장했습니다.", null);
    }

    @DeleteMapping("/images")
    @Operation(
            summary = "홍보 사진 삭제",
            description = "홍보 사진 삭제 (해당 종류 1장 삭제)"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "이미지 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ApiResponse<DeleteImageResponseDto> deleteMainpageImage(
//            @Parameter(description = "인증 토큰", required = true)
//            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody DeleteImageRequestDto request
    ) {
        // TODO: 이미지 삭제 로직 구현
        return ApiResponse.onSuccess(request.getType().getDescription() + " 이미지를 삭제했습니다.", null);
    }

    // Inquiry Link APIs
    @GetMapping("/inquiry-link")
    @Operation(
            summary = "문의하기(카카오톡) 링크 조회",
            description = "문의하기(카카오톡) 링크 조회"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "링크 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ApiResponse<InquiryLinkResponseDto> getInquiryLink(
//            @Parameter(description = "인증 토큰", required = true)
//            @RequestHeader("Authorization") String authorization
    ) {
        // TODO: 문의하기 링크 조회 로직 구현
        return ApiResponse.onSuccess("문의하기 링크를 조회했습니다.", null);
    }

    @PostMapping("/inquiry-link")
    @Operation(
            summary = "문의하기(카카오톡) 링크 추가/수정",
            description = "문의하기(카카오톡) 링크 추가/수정"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "링크 저장 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (URL 형식 오류, 필수 필드 누락)",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ApiResponse<InquiryLinkResponseDto> upsertInquiryLink(
//            @Parameter(description = "인증 토큰", required = true)
//            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody InquiryLinkRequestDto request
    ) {
        // TODO: 문의하기 링크 추가/수정 로직 구현
        return ApiResponse.onSuccess("문의하기 링크를 저장했습니다.", null);
    }

    @DeleteMapping("/inquiry-link")
    @Operation(
            summary = "문의하기(카카오톡) 링크 삭제",
            description = "문의하기(카카오톡) 링크 삭제"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "링크 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ApiResponse<DeleteLinkResponseDto> deleteInquiryLink(
//            @Parameter(description = "인증 토큰", required = true)
//            @RequestHeader("Authorization") String authorization
    ) {
        // TODO: 문의하기 링크 삭제 로직 구현
        return ApiResponse.onSuccess("문의하기 링크를 삭제했습니다.", null);
    }

    // Reviews APIs
    @GetMapping("/reviews")
    @Operation(
            summary = "후기 카드 전체 조회",
            description = "후기 카드 전체 조회 (순위/표시 여부 포함)"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "후기 카드 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ApiResponse<List<ReviewResponseDto>> getReviews(
//            @Parameter(description = "인증 토큰", required = true)
//            @RequestHeader("Authorization") String authorization
    ) {
        // TODO: 후기 카드 전체 조회 로직 구현
        return ApiResponse.onSuccess("후기 카드를 조회했습니다.", null);
    }

    @PutMapping("/reviews/order")
    @Operation(
            summary = "후기 카드 순위 일괄 변경",
            description = "후기 카드 순위 일괄 변경"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "순서 변경 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ApiResponse<ReorderResponseDto> reorderReviews(
//            @Parameter(description = "인증 토큰", required = true)
//            @RequestHeader("Authorization") String authorization,
            @Valid @RequestBody ReorderRequestDto request
    ) {
        // TODO: 후기 카드 순위 변경 로직 구현
        return ApiResponse.onSuccess("후기 카드 순서를 갱신했습니다.", null);
    }

    @DeleteMapping("/reviews/{reviewId}")
    @Operation(
            summary = "후기 카드 삭제",
            description = "후기 카드 삭제"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "후기 카드 삭제 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "후기를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ApiResponse<DeleteReviewResponseDto> deleteReview(
//            @Parameter(description = "인증 토큰", required = true)
//            @RequestHeader("Authorization") String authorization,
            @Parameter(description = "후기 ID", required = true)
            @PathVariable("reviewId") String reviewId
    ) {
        // TODO: 후기 카드 삭제 로직 구현
        return ApiResponse.onSuccess("후기 카드를 삭제했습니다.", null);
    }
}
