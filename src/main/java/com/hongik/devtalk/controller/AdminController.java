package com.hongik.devtalk.controller;

import com.hongik.devtalk.domain.Admin;
import com.hongik.devtalk.domain.login.admin.AdminDTO;
import com.hongik.devtalk.domain.login.admin.AdminLoginDTO;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.admin.AdminCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.hongik.devtalk.domain.login.admin.AdminDTO.toJoinAdminResDTO;
import static com.hongik.devtalk.domain.login.admin.AdminDTO.toLoginIdResDTOList;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@Tag(name="Admin",description = "관리자 페이지 API")
public class AdminController {

    private final AdminCommandService adminCommandService;

    @GetMapping("/authority/loginIds")
    @Operation(summary = "관리자 아이디 관리 API -by 남성현", description = "관리자의 아이디 리스트를 조회합니다.")
    public ApiResponse<AdminDTO.LoginIdResDTOList> loginIdList() {

        return ApiResponse.onSuccess("관리자 아이디 리스트", toLoginIdResDTOList(null));
    }

    @PostMapping("/authority/loginIds")
    @Operation(summary = "관리자 아이디 추가 API -by 남성현", description = "추가할 관리자의 아이디와 비밀번호를 입력합니다.")
    public ApiResponse<AdminDTO.JoinAdminResDTO> joinAdmin(@RequestBody @Valid AdminLoginDTO.LoginReqDTO request) {

        Admin admin = adminCommandService.joinAdmin(request);

        return ApiResponse.onSuccess("관리자 추가 완료", toJoinAdminResDTO(admin));
    }

    @DeleteMapping("/authority/loginIds/{adminId}")
    @Operation(summary = "관리자 아이디 삭제 API -by 남성현", description = "선택한 관리자 아이디를 삭제합니다.")
    @Parameters({@Parameter(name="adminId",description = "삭제할 관리자의 id입니다.")})
    public ApiResponse<Void> deleteAdmin(@PathVariable("adminId") Long adminId) {

        return ApiResponse.onSuccess("삭제 완료");
    }
}
