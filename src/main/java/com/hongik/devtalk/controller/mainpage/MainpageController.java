package com.hongik.devtalk.controller.mainpage;

import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.domain.mainpage.MainpageImagesResponseDto;
import com.hongik.devtalk.service.mainpage.MainpageImagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Mainpage", description = "홈페이지 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/home")
public class MainpageController {

    private final MainpageImagesService mainpageImagesService;

    @GetMapping("/images")
    @Operation(
            summary = "홍보 사진 현재 상태 조회",
            description = "Devtalk 소개 사진과 이전 세미나 보러가기 사진(각 1장)의 현재 상태를 조회합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "홍보 사진 정보 조회 성공",
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
    public ApiResponse<MainpageImagesResponseDto> getMainpageImages(
            @Parameter(description = "인증 토큰", required = true)
            @RequestHeader("Authorization") String authorization
    ) {
        MainpageImagesResponseDto result = mainpageImagesService.getMainpageImages();
        return ApiResponse.onSuccess("홍보 사진 정보를 조회했습니다.", result);
    }
}
