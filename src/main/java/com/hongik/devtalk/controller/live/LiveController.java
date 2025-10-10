package com.hongik.devtalk.controller.live;

import com.hongik.devtalk.domain.live.dto.*;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.live.LiveService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@Tag(name = "Live", description = "세미나 라이브 관련 API -by 황신애")
@RequestMapping("/user/live")
@RequiredArgsConstructor
public class LiveController {
    private final LiveService liveService;

    @PostMapping("/auth")
    @Operation(summary = "신청자 인증 API", description = "학번과 이름을 받아 세미나 신청자임을 인증하고, 라이브 서비스 접근을 위한 JWT 토큰을 발급합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "인증을 위한 학생의 학번과 이름",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthStudentRequestDto.class),
                    examples = @ExampleObject(name = "인증 요청", value = "{\"studentNum\": \"C211182\", \"name\": \"황신애\"}")
            )
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "OK, 인증 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(name = "인증 성공",
                                    value = "{\"isSuccess\": true, \"code\": \"COMMON2000\", \"message\": \"신청자 인증에 성공하였습니다.\", \"result\": {\"studentId\": 1, \"seminarId\": 1, \"accessToken\": \"your-access-token\", \"refreshToken\": \"your-refresh-token\"}}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404", description = "NOT_FOUND, 요청 처리 불가",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "학생 정보 없음", value = "{\"isSuccess\": false, \"code\": \"STUDENT_4041\", \"message\": \"해당 학생을 찾을 수 없습니다.\", \"result\": null}"),
                                    @ExampleObject(name = "신청 정보 없음", value = "{\"isSuccess\": false, \"code\": \"APPLICANT_4041\", \"message\": \"해당 학생의 신청 정보를 찾을 수 없습니다.\", \"result\": null}"),
                                    @ExampleObject(name = "인증 기간 아님", value = "{\"isSuccess\": false, \"code\": \"SEMINAR_4041\", \"message\": \"세미나 진행 및 리뷰작성 기간이 아닙니다\", \"result\": null}")
                            }))
    })
    public ApiResponse<AuthStudentResponseDto> authStudent(@RequestBody AuthStudentRequestDto authStudentRequestDto) {
        return liveService.authStudent(authStudentRequestDto);
    }

    @Operation(summary = "토큰 재발급 API", description = "Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급합니다.")
    @ApiResponses({
            // 👇 [수정] 성공(200) 응답에 'examples' 속성을 추가합니다.
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
                    content = @Content(
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "Success-Example",
                                    value = "{\"isSuccess\":true,\"code\":\"COMMON2000\",\"message\":\"토큰이 성공적으로 재발급되었습니다.\",\"result\":{\"accessToken\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDMDEyMzQ1Iiwicm9sZSI6IlJPTEVfVVNFUiIsInR5cGUiOiJhY2Nlc3NfdG9rZW4iLCJpYXQiOjE3NTk1ODczODksImV4cCI6MTc1OTU4OTE4OX0.buR867uUg6i8lu4sywTlfSYZl0RmQZ8W_-EN0Y0YKvY\",\"refreshToken\":\"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJDMDEyMzQ1IiwidHlwZSI6InJlZnJlc2hfdG9rZW4iLCJpYXQiOjE3NTk1ODczODksImV4cCI6MTc2MDE5MjE4OX0.ZDjD-UAmvPCcIik0CrHdbZut2HWR3KG1ADtsIu9KaaA\"},\"error\":null}"
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "유효하지 않은 Refresh Token 입니다. (AUTH_4012)", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Refresh Token에 해당하는 학생 정보를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @PostMapping("/reissue")
    public ApiResponse<ReissueResponseDto> reissue(@RequestBody ReissueRequestDto reissueRequestDto) {
        ReissueResponseDto reissueResponseDto = liveService.reissueToken(reissueRequestDto);
        return ApiResponse.onSuccess("토큰이 성공적으로 재발급되었습니다.", reissueResponseDto);
    }

    @PostMapping("/review")
    @Operation(summary = "세미나 리뷰 작성 API", description = "세미나 종료 후 리뷰를 작성합니다. Authorization 헤더에 Bearer 토큰이 필요합니다.",
            security = {@SecurityRequirement(name = "JWT TOKEN")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "작성할 리뷰의 내용",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ReviewRequestDto.class),
                    examples = @ExampleObject(name = "리뷰 작성 요청", value = "{\"strength\": \"너무 유익하고 좋은 강의였습니다\", \"improvement\": \"강의실이 너무 추웠어요\", \"nextTopic\": \"AI 활용에 대해서 강연 듣고 싶어요\", \"score\": 4, \"isPublic\": true}")
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
    public ApiResponse<ReviewResponseDto> createReview(@AuthenticationPrincipal User user,
                                                       @RequestBody ReviewRequestDto requestDto) {
        String studentNum = user.getUsername();
        return liveService.createReview(studentNum, requestDto);
    }

    @PostMapping("/attend")
    @Operation(summary = "세미나 라이브 출석 체크 API", description = "세미나 라이브 출석을 체크하고, 성공 시 라이브 URL을 반환합니다. Authorization 헤더에 Bearer 토큰이 필요합니다.",
            security = {@SecurityRequirement(name = "JWT TOKEN")})
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "OK, 출석 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = {
                                    @ExampleObject(name = "출석 성공", value = "{\"isSuccess\": true, \"code\": \"COMMON2000\", \"message\": \"성공적으로 출석 인증 되었습니다.\", \"result\": {\"liveUrl\": \"https://youtube.com/hongik_dev\", \"attendanceStatus\": \"PRESENT\"}}"),
                                    @ExampleObject(name = "이미 출석 처리됨", value = "{\"isSuccess\": true, \"code\": \"COMMON2000\", \"message\": \"이미 출석 처리된 사용자입니다.\", \"result\": {\"liveUrl\": \"https://youtube.com/hongik_dev\", \"attendanceStatus\": \"PRESENT\"}}")
                            })),
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
                                    @ExampleObject(name = "출석 시간 아님", value = "{\"isSuccess\": false, \"code\": \"ATTEND_4041\", \"message\": \"출석체크 기간이 아닙니다.\", \"result\": null}"),
                                    @ExampleObject(name = "라이브 URL 없음", value = "{\"isSuccess\": false, \"code\": \"SEMINARLIVE_4041\", \"message\": \"세미나 라이브 URL이 아직 등록되지 않았습니다\", \"result\": null}"),
                                    @ExampleObject(name = "학생 정보 없음", value = "{\"isSuccess\": false, \"code\": \"STUDENT_4041\", \"message\": \"해당 학생을 찾을 수 없습니다.\", \"result\": null}")
                            }))
    })
    public ApiResponse<AttendanceResponseDto> attendCheck(@AuthenticationPrincipal User user) {
        String studentNum = user.getUsername();
        LocalDateTime attendTime = LocalDateTime.now();
        return liveService.attendanceCheck(studentNum, attendTime);
    }
}
