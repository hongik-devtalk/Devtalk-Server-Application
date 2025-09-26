package com.hongik.devtalk.controller.auth;

import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.controller.auth.dto.LogoutRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AuthController {

    @PostMapping("/logout")
    @Operation(
            summary = "관리자 로그아웃",
            description = "관리자 로그아웃 API 호출 (로그아웃 후 '관리자 로그인' 페이지로 이동하도록 프런트 리다이렉션)"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공",
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
    public ApiResponse<Void> logout(
            @Parameter(description = "인증 토큰", required = true)
            @RequestHeader("Authorization") String authorization,
            @RequestBody LogoutRequestDto logoutRequest
    ) {
        // TODO: 로그아웃 로직 구현
        return ApiResponse.onSuccess("정상적으로 로그아웃되었습니다.");
    }
}
