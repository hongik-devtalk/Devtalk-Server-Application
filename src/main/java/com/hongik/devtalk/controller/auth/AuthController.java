package com.hongik.devtalk.controller.auth;

import com.hongik.devtalk.domain.login.admin.AdminLoginDTO;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.domain.auth.dto.LogoutRequestDto;
import com.hongik.devtalk.service.admin.AdminCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
public class AuthController {

    private final AdminCommandService adminCommandService;

    @PostMapping("/login")
    @Operation(summary = "관리자 로그인 API -by 남성현", description = "아이디와 비밀번호를 받아 로그인 기능 수행합니다.")
    public ApiResponse<AdminLoginDTO.LoginResDTO> login(@RequestBody @Valid AdminLoginDTO.LoginReqDTO request) {

        return ApiResponse.onSuccess("로그인에 성공하였습니다.", adminCommandService.loginAdmin(request));
    }

    @PostMapping("/refresh")
    @Operation(summary = "관리자 accessToken 재발급 API -by 남성현", description = "리프레시 토큰을통해 엑세스 토큰 재발급")
    public ApiResponse<AdminLoginDTO.LoginResDTO> refresh(@RequestHeader("refreshToken") String refreshToken) {

        return ApiResponse.onSuccess("accessToken 재발급 성공.", adminCommandService.refreshAdmin(refreshToken));
    }

    @PostMapping("/logout")
    @Operation(
            summary = "관리자 로그아웃 - by 남성현, 이태훈",
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
            @RequestHeader(name = "Authorization", required = false) String authHeader,
            @RequestBody LogoutRequestDto refreshTokenDTO) {

        //refreshToken 폐기
        if (refreshTokenDTO.getRefreshToken() != null && !refreshTokenDTO.getRefreshToken().isBlank()) {
            adminCommandService.deleteRefreshToken(refreshTokenDTO.getRefreshToken());
        }

        return ApiResponse.onSuccess("정상적으로 로그아웃되었습니다.");
    }
}
