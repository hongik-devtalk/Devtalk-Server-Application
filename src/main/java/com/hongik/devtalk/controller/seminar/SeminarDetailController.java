package com.hongik.devtalk.controller.seminar;

import com.hongik.devtalk.domain.seminar.detail.dto.SeminarDetailResponseDto;
import com.hongik.devtalk.domain.seminar.detail.dto.SeminarDetailReviewResponseDto;
import com.hongik.devtalk.domain.seminar.detail.dto.SeminarDetailSessionResponseDto;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.seminar.SeminarDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name="SeminarDetail",description = "세미나 세부정보 관련 API")
@RestController
@RequestMapping("/user/seminars")
@RequiredArgsConstructor
public class SeminarDetailController {

    private final SeminarDetailService seminarDetailService;

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
    public ApiResponse<SeminarDetailResponseDto> getSeminars(@PathVariable Long seminarId)
    {

        SeminarDetailResponseDto seminarDetailInfo = seminarDetailService.getSeminarDetail(seminarId);

        return ApiResponse.onSuccess("세미나 세부정보 ( 세미나 ) 조회에 성공하였습니다. ",seminarDetailInfo);

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


}
