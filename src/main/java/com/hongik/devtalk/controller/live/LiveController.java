package com.hongik.devtalk.controller.live;

import com.hongik.devtalk.domain.Student;
import com.hongik.devtalk.domain.live.dto.*;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.live.LiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Tag(name = "Live", description = "세미나 라이브 관련 API")
@RequestMapping("/user/live")
@RequiredArgsConstructor
public class LiveController {
    private final LiveService liveService;

    @PostMapping("/auth")
    @Operation(summary = "신청자 인증 API", description = "신청자를 인증하고 JWT 토큰을 발급합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "OK, 인증 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "성공 응답 예시",
                                    value = """
                        {
                            "isSuccess": true,
                            "code": "COMMON2000",
                            "message": "신청자 인증에 성공하였습니다.",
                            "result": {
                                "studentId": 1,
                                "seminarId": 1,
                                "accessToken": "accessToken",
                                "refreshToken": "refreshToken"
                            "error": null
                        }
                        """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "AUTH_4011", description = "신청자 정보 없음",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "인증 실패",
                                    value = "{\"isSuccess\": false, \"code\": \"AUTH_4011\", \"message\": \"일치하는 신청자 정보가 없습니다.\", \"result\": null, \"error\": {\"reason\": \"Applicant not found\", \"hint\": \"학번과 이름을 다시 확인해주세요.\"}}"
                            )))
    })
    public ApiResponse<AuthStudentResponseDto> authStudent(@RequestBody AuthStudentRequestDto authStudentRequestDto) {
        return liveService.authStudent(authStudentRequestDto);
    }

    @PostMapping("/review")
    @Operation(summary = "세미나 리뷰 작성 API", description = "세미나 종료 후 리뷰를 작성합니다. Authorization 헤더에 accessToken이 필요합니다.",
            security = { @SecurityRequirement(name = "bearer-key") }) // Swagger Security Scheme 이름
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "OK, 리뷰 작성 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "AUTH_4191", description = "토큰 만료",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "토큰 만료 예시",
                                    value = "{\"isSuccess\": false, \"code\": \"AUTH_4191\", \"message\": \"토큰이 만료되었습니다.\", \"result\": null, \"error\": {\"reason\": \"Expired JWT\", \"hint\": \"refreshToken으로 재발급 후 다시 요청하세요.\"}}"
                            )))
    })
    public ApiResponse<ReviewResponseDto> createReview(@AuthenticationPrincipal User user,
                                                       @RequestBody ReviewRequestDto requestDto) {
        String studentNum =user.getUsername();
        return liveService.createReview(studentNum, requestDto);
    }

    @PostMapping("/attend")
    @Operation(summary = "세미나 라이브 출석 체크 API", description = "세미나 라이브 출석을 체크하고, 성공 시 라이브 URL을 반환합니다. Authorization 헤더에 accessToken이 필요합니다.",
            security = { @SecurityRequirement(name = "bearer-key") })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "OK, 출석 성공",
                    // implementation을 위에서 만든 AttendanceResponse.class로 변경
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "AUTH_4191", description = "토큰 만료",
                    // content에 examples를 사용하여 직접 JSON 예시를 추가
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "토큰 만료 예시",
                                    value = "{\"isSuccess\": false, \"code\": \"AUTH_4191\", \"message\": \"토큰이 만료되었습니다.\", \"result\": null, \"error\": {\"reason\": \"Expired JWT\", \"hint\": \"refreshToken으로 재발급 후 다시 요청하세요.\"}}"
                            )
                    )
            )
    })
    public ApiResponse<AttendanceResponseDto> attendCheck(@AuthenticationPrincipal User user) {
        String studentNum = user.getUsername();
        LocalDateTime attendTime = LocalDateTime.now();
        return liveService.attendanceCheck(studentNum, attendTime);
    }
}
