package com.hongik.devtalk.controller.seminar;

import com.hongik.devtalk.domain.seminar.dto.SeminarListDto;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.seminar.SeminarListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "SeminarList", description = "세미나 리스트 조회 API")
@RequestMapping("/user/seminarList")
public class SeminarListController {

    private final SeminarListService seminarListService;

    @GetMapping("/")
    @Operation(summary = "세미나 리스트 조회 API", description = "지난 회차부터 현재 회차까지의 신청정보 처리")
    public ApiResponse<SeminarListDto.SeminarResDtoList> getSeminarList() {
        return ApiResponse.onSuccess("세미나 리스트 조회에 성공하였습니다.", seminarListService.seminarList());
    }
}
