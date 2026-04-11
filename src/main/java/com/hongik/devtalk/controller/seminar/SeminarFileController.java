package com.hongik.devtalk.controller.seminar;

import com.hongik.devtalk.domain.seminar.dto.SeminarFileResponseDto;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.seminar.SeminarVideoLoadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user/seminars/")
@RestController
@RequiredArgsConstructor
public class SeminarFileController {

    private final SeminarVideoLoadService seminarVideoLoadService;

    @Operation(summary = "세미나 자료 리스트 조회", description = "특정 세미나의 모든 첨부 자료(PDF, 이미지 등) 정보를 가져옴")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "세미나를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/{seminarId}/files")
    public ApiResponse<SeminarFileResponseDto> getSeminarFiles(
            @Parameter(description = "세미나 고유 ID", example = "1")
            @PathVariable Long seminarId) {

        SeminarFileResponseDto response = seminarVideoLoadService.getSeminarFile(seminarId);
        return ApiResponse.onSuccess("seminar file 반환 성공",response);
    }
}
