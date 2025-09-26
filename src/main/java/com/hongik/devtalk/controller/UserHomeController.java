package com.hongik.devtalk.controller;

import com.hongik.devtalk.dto.SeminarInfoResponseDTO;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.SeminarInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user/home")
@Tag(name="유저 홈화면")
public class UserHomeController {

    private final SeminarInfoService seminarInfoService;

    @GetMapping("/{seminarId}")
    @Operation(summary = "현재 신청받고 있는 세미나 정보를 보여줍니다.")
    public ResponseEntity<ApiResponse<SeminarInfoResponseDTO>> getSeminar(@PathVariable Long seminarId){
        SeminarInfoResponseDTO result = seminarInfoService.getSeminarById(seminarId);
        return ResponseEntity.ok(ApiResponse.onSuccess("",result));
    }

}
