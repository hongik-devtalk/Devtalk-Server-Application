package com.hongik.devtalk.controller.seminar;


import com.hongik.devtalk.domain.live.dto.ReviewRequestDto;
import com.hongik.devtalk.domain.live.dto.ReviewResponseDto;
import com.hongik.devtalk.domain.seminar.dto.SeminarArchiveReviewRequestDto;
import com.hongik.devtalk.domain.seminar.dto.SeminarArchiveReviewResponseDto;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.seminar.SeminarArchiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Seminar", description = "세미나 아카이브 관련 API - by 박서영")
@RequiredArgsConstructor
@RestController
@RequestMapping("/archive/seminars")
public class SeminarArchiveController {

    private final SeminarArchiveService seminarArchiveService;


    @PostMapping("{seminarId}/reviews")
    @Operation(summary = "세미나 아카이브 리뷰 작성 API", description = "신청했던 세미나의 리뷰를 작성합니다. Authorization 헤더에 Bearer 토큰이 필요합니다.",
            security = {@SecurityRequirement(name = "JWT TOKEN")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "작성할 리뷰의 내용",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SeminarArchiveReviewRequestDto.class),
                    examples = @ExampleObject(name = "리뷰 작성 요청", value = "{\"totalContent\": \"너무 유익하고 좋은 강의였습니다\", \"score\": 4}")
            )
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "OK, 리뷰 작성 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "리뷰 작성 성공",
                                    value = "{\"isSuccess\": true, \"code\": \"COMMON2000\", \"message\": \"성공적으로 리뷰가 등록되었습니다.\", \"result\": {\"reviewId\": 1, \"studentNum\": \"C211182\", \"seminarNum\": 1, \"seminarId\": 1}}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401", description = "UNAUTHORIZED, 토큰 만료",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "토큰 만료",
                                    value = "{\"isSuccess\": false, \"code\": \"AUTH_4191\", \"message\": \"토큰이 만료되었습니다.\", \"result\": null}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "NOT_FOUND, 요청 처리 불가",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "리뷰 기간 아님", value = "{\"isSuccess\": false, \"code\": \"REVIEW_4041\", \"message\": \"리뷰 작성 기간이 아닙니다.\", \"result\": null}"),
                                    @ExampleObject(name = "학생 정보 없음", value = "{\"isSuccess\": false, \"code\": \"STUDENT_4041\", \"message\": \"해당 학생을 찾을 수 없습니다.\", \"result\": null}")
                            })),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409", description = "CONFLICT, 리소스 충돌",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "리뷰 중복 작성", value = "{\"isSuccess\": false, \"code\": \"REVIEW_4091\", \"message\": \"리뷰는 한 번만 작성 가능합니다.\", \"result\": null}")
                            }))
    })
    public ApiResponse<SeminarArchiveReviewResponseDto> createArchiveReview(@AuthenticationPrincipal User user,
                                                                     @PathVariable Long seminarId,
                                                                     @RequestBody SeminarArchiveReviewRequestDto requestDto) {
        String studentNum = user.getUsername();
        return seminarArchiveService.createArchiveReview(studentNum, seminarId ,requestDto);
    }


}
