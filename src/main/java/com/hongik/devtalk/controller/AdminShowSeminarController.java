package com.hongik.devtalk.controller;

import com.hongik.devtalk.domain.showseminar.dto.ShowSeminarRequestDTO;
import com.hongik.devtalk.domain.showseminar.dto.ShowSeminarResponseDTO;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.ShowSeminarService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/show-seminar")
@Tag(name="[Admin] ShowSeminar",description = "어드민 화면 노출 회차 관리 - by 박소연")
public class AdminShowSeminarController {
    private final ShowSeminarService showSeminarService;

    @PostMapping
    public ResponseEntity<ApiResponse<ShowSeminarResponseDTO>> updateShowSeminar(@RequestBody ShowSeminarRequestDTO request){
        ShowSeminarResponseDTO response = showSeminarService.updateShowSeminar(request);
        return ResponseEntity.ok(ApiResponse.onSuccess("노출 회차 업데이트 성공", response));

    }

}
