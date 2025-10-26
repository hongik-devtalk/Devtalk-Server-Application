package com.hongik.devtalk.controller;

import com.hongik.devtalk.domain.ShowSeminar;
import com.hongik.devtalk.domain.showseminar.dto.ShowSeminarResponseDTO;
import com.hongik.devtalk.domain.userhome.dto.UserHomeSeminarInfoResponseDTO;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.ShowSeminarService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/show-seminar")
@Tag(name="[User] ShowSeminar",description = "유저 홈화면에서 노출할 세미나 회차 응답 api - by 박소연")
public class ShowSeminarController {
    private final ShowSeminarService showSeminarService;

    @GetMapping
    public ResponseEntity<ApiResponse<ShowSeminarResponseDTO>> getCurrentShowSeminar(){
        ShowSeminarResponseDTO response = showSeminarService.getCurrentShowSeminar();
        return ResponseEntity.ok(ApiResponse.onSuccess("홈화면 노출 세미나 정보 조회 성공", response));
    }
}
