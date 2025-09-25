package com.hongik.devtalk.controller;

import com.hongik.devtalk.dto.SeminarResponseDTO;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user/home")
@Tag(name="유저 홈화면")
public class UserHomeController {
    @GetMapping("/{serminarId}")
    @Operation(summary = "현재 신청받고 있는 세미나 정보를 보여줍니다.")
    public ResponseEntity<ApiResponse<SeminarResponseDTO>> getSeminar(@PathVariable Long seminarId){
        SeminarResponseDTO dummy = SeminarResponseDTO.builder()
                .seminarId(seminarId)
                .seminarNum(1)
                .topic("LLM")
                .seminarDate(LocalDateTime.now())
                .place("T동")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(ApiResponse.onSuccess("스웨거 테스트",dummy));
    }

    //@GetMapping("/")


}
