package com.hongik.devtalk.controller;

import com.hongik.devtalk.domain.seminar.admin.dto.AdminStatsResponseDTO;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.seminar.AdminStatsService;
import com.hongik.devtalk.service.seminar.SearchStatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/stats")
@Tag(name = "[Admin] Statistics", description = "관리자 통계 API - by 양지윤")
@SecurityRequirement(name = "JWT TOKEN")
public class AdminStatsController {

    private final AdminStatsService adminStatsService;

    @GetMapping("/seminars/{seminarId}/views")
    @Operation(
            summary = "세미나 카드 조회수 통계 조회",
            description = "특정 세미나 카드의 기간별 일자 단위 조회수 통계를 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "세미나 카드 조회수 통계 조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "날짜 형식 또는 요청 파라미터가 잘못된 경우",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "관리자 인증이 필요한 경우",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "세미나를 찾을 수 없는 경우",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ApiResponse<AdminStatsResponseDTO.SeminarViewsResponseDTO> seminarViews(
            @Parameter(description = "조회할 세미나 ID", required = true, example = "1")
            @PathVariable Long seminarId,
            @Parameter(
                    description = "조회 시작일",
                    required = true,
                    schema = @Schema(type = "string", format = "date", example = "2026-03-01")
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(
                    description = "조회 종료일",
                    required = true,
                    schema = @Schema(type = "string", format = "date", example = "2026-03-31")
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        AdminStatsResponseDTO.SeminarViewsResponseDTO response = adminStatsService.getSeminarViews(seminarId, from, to);
        return ApiResponse.onSuccess("세미나 카드 조회수 통계 조회 성공", response);
    }


    @GetMapping("/search/top5")
    @Operation(
            summary = "검색어 통계 조회",
            description = "기간 내 검색어 TOP 5 통계를 조회합니다. target은 ALL, SEMINAR, SPEAKER 중 하나를 사용합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "검색어 통계 조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "날짜 형식 또는 요청 파라미터가 잘못된 경우",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "관리자 인증이 필요한 경우",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ApiResponse<AdminStatsResponseDTO.SearchKeywordStatsResponseDTO> top5(
            @Parameter(
                    description = "조회 시작일",
                    required = true,
                    schema = @Schema(type = "string", format = "date", example = "2026-03-01")
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(
                    description = "조회 종료일",
                    required = true,
                    schema = @Schema(type = "string", format = "date", example = "2026-03-31")
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @Parameter(
                    description = "검색 대상 구분",
                    schema = @Schema(
                            allowableValues = {
                                    SearchStatsService.TARGET_ALL,
                                    SearchStatsService.TARGET_SEMINAR,
                                    SearchStatsService.TARGET_SPEAKER
                            },
                            defaultValue = SearchStatsService.TARGET_ALL
                    ),
                    example = "ALL"
            )
            @RequestParam(defaultValue = SearchStatsService.TARGET_ALL) String target
    ) {
        AdminStatsResponseDTO.SearchKeywordStatsResponseDTO response =
                adminStatsService.getSearchKeywordStats(from, to, target);
        return ApiResponse.onSuccess("검색어 통계 조회 성공", response);
    }
}
