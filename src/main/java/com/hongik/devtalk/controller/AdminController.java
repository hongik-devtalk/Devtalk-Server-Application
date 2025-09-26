package com.hongik.devtalk.controller;

import com.hongik.devtalk.domain.login.AdminLoginDTO;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.admin.AdminCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@Tag(name="Admin",description = "관리자 페이지 API")
public class AdminController {

    private final AdminCommandService adminCommandService;

    @PostMapping("/login")
    @Operation(summary = "관리자 로그인 API -by 남성현", description = "아이디와 비밀번호를 받아 로그인 기능 수행합니다.")
    public ApiResponse<AdminLoginDTO.LoginResDTO> login(@RequestBody @Valid AdminLoginDTO.LoginReqDTO request) {

        return ApiResponse.onSuccess("로그인에 성공하였습니다.", adminCommandService.loginAdmin(request));
    }

}
