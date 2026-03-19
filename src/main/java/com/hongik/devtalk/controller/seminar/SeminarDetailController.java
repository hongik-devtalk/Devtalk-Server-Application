package com.hongik.devtalk.controller.seminar;

import com.hongik.devtalk.domain.seminar.detail.dto.*;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.seminar.SeminarDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.hongik.devtalk.service.seminar.SeminarViewStatsService;
import com.hongik.devtalk.service.seminar.SearchStatsService;
import org.springframework.web.bind.annotation.RequestHeader;
import java.time.LocalDateTime;
import java.util.List;

@Tag(name="SeminarDetail",description = "세미나 세부정보 관련 API - by 박서영")
@RestController
@RequestMapping("/user/seminars")
@RequiredArgsConstructor
public class SeminarDetailController {

    private final SeminarDetailService seminarDetailService;
    private final SeminarViewStatsService seminarViewStatsService;
    private final SearchStatsService searchStatsService;

    //세미나 세부정보 조회( 세미나 )

    @Operation(summary = "세미나 세부정보 (세미나) 조회", description = "seminarId를 사용하여 해당 세미나의 정보를 조회합니다.")
    @GetMapping("/{seminarId}")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(

                    responseCode = "200",
                    description = "세미나 세부정보 (세미나) 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))

            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "해당 ID의 세미나를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))

            )
    })
    public ApiResponse<SeminarDetailResponseDto> getSeminars(
            @PathVariable Long seminarId,
            @RequestHeader(value="X-Client-Id", required=false) String clientId
    ){
        SeminarDetailResponseDto seminarDetailInfo = seminarDetailService.getSeminarDetail(seminarId);

        //본문 조회 성공 후 +1
        seminarViewStatsService.recordSeminarView(seminarId, clientId);

        return ApiResponse.onSuccess("세미나 세부정보 ( 세미나 ) 조회에 성공하였습니다. ", seminarDetailInfo);
    }

    //세미나 세부정보 조회 ( 리뷰 )

    @Operation(summary = "세미나 세부정보 (리뷰) 조회", description = "seminarId를 사용하여 해당 세미나의 리뷰 목록을 조회합니다.")
    @GetMapping("/{seminarId}/review")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(

                    responseCode = "200",
                    description = "세미나 세부정보 ( 리뷰 ) 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))

            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "해당 ID의 세미나를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))

            )
    })
    public ApiResponse<List<SeminarDetailReviewResponseDto>> getSeminarReviews(@PathVariable Long seminarId)
    {

        List<SeminarDetailReviewResponseDto> reviewList=seminarDetailService.getSeminarDetailReview(seminarId);
        return ApiResponse.onSuccess("세미나 세부정보 (리뷰) 조회에 성공하였습니다.", reviewList);

    }


    //세미나 세부정보 조회 ( 세션 )


    @Operation(summary = "세미나 세부정보 (세션) 조회", description = "seminarId를 사용하여 해당 세미나의 세션 목록을 조회합니다.")
    @GetMapping("/{seminarId}/session")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(

                    responseCode = "200",
                    description = "세미나 세부정보 ( 리뷰 ) 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))

            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "해당 ID의 세미나를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))

            )
    })

    public ApiResponse<List<SeminarDetailSessionResponseDto>> getSeminarSessions(@PathVariable Long seminarId)
    {
        List<SeminarDetailSessionResponseDto> sessionList=seminarDetailService.getSeminarDetailSession(seminarId);
        return ApiResponse.onSuccess("세미나 세션 목록 조회에 성공하였습니다.", sessionList);

    }

    //세미나 검색


    @Operation(summary = "세미나 검색 ", description = "세미나를 키워드로 검색하여 해당 세미나의 정보를 조회합니다.")
    @GetMapping("/search")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(

                    responseCode = "200",
                    description = "세미나 검색 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))

            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청( 키워드 누락 )",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))

            )
    })

    public ApiResponse<List<SeminarSearchResponseDto>> searchSeminars(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestHeader(value="X-Client-Id", required=false) String clientId
    ){
        //검색 요청 들어온 순간 +1
        searchStatsService.recordSearch(SearchStatsService.TARGET_SEMINAR, keyword, clientId);

        List<SeminarSearchResponseDto> seminarList = seminarDetailService.searchSeminars(keyword);
        return ApiResponse.onSuccess("세미나 검색에 성공하였습니다.", seminarList);
    }


}