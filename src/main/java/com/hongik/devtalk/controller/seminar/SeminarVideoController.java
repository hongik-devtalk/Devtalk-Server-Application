package com.hongik.devtalk.controller.seminar;

import com.hongik.devtalk.domain.seminar.dto.SeminarVideoResponseDTO;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.seminar.SeminarVideoLoadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Video", description = "세미나 영상 조회 API - by 박서영 ")
@RequestMapping("/user/seminars/video")
public class SeminarVideoController {


    private final SeminarVideoLoadService seminarVideoLoadService;

    @Operation(summary = "세미나 녹화본 영상 링크 조회 - by 박서영 ", description = "세미나 녹화본 영상 링크 조회 api입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "세미나 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @GetMapping("/{seminarId}")
    public ApiResponse<SeminarVideoResponseDTO> createSeminarVideoUrl(
            @Parameter(description = "세미나 ID", required = true)
            @PathVariable Long seminarId
    ) {

        SeminarVideoResponseDTO response = seminarVideoLoadService.getSeminarVideo(seminarId);

        return ApiResponse.onSuccess("세미나 녹화본 영상 링크 조회에 성공했습니다.",response);
    }
}

