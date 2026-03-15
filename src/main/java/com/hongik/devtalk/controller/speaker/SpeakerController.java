package com.hongik.devtalk.controller.speaker;


import com.hongik.devtalk.domain.speaker.dto.SpeakerDetailResponseDto;
import com.hongik.devtalk.domain.speaker.dto.SpeakerSearchResponseDto;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.speaker.SpeakerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name="Speaker",description = "연사 정보 조회 관련 API - by 박서영")
@RestController
@RequestMapping("/user/speakers")
@RequiredArgsConstructor
public class SpeakerController {

    private final SpeakerService speakerService;


    //세미나 연사 검색


    @Operation(summary = "세미나 연사 검색 ", description = "연사를 검색하여 해당 연사의 정보를 조회합니다.")
    @GetMapping("/search")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(

                    responseCode = "200",
                    description = "연사 검색 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))

            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청( 키워드 누락 )",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))

            )
    })

    public ApiResponse<List<SpeakerSearchResponseDto>> searchSpeakers(@RequestParam(value = "keyword", required = false) String keyword)
    {
        List<SpeakerSearchResponseDto> speakerList = speakerService.searchSpeakers(keyword);

        return ApiResponse.onSuccess("연사 검색에 성공하였습니다.",speakerList);
    }


    @Operation(summary = " 연사 목록 조회 ", description = "연사의 목록을 조회합니다.")
    @GetMapping
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(

                    responseCode = "200",
                    description = "연사 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))

            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })


    //연사 목록 조회

    public ApiResponse<List<SpeakerSearchResponseDto>> getSpeakers()
    {
        //모든 연사 호출
        List<SpeakerSearchResponseDto> speakers = speakerService.getAllSpeakers();

        return ApiResponse.onSuccess("연사 목록 조회에 성공했습니다",speakers);
    }

    //연사 상세정보 조회
    @Operation(summary = "연사 상세정보 조회", description = "특정 연사의 상세 정보를 조회합니다.")
    @GetMapping("/{speakerId}") // 경로 변수 사용
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "연사 상세정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "연사를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })

    public ApiResponse<SpeakerDetailResponseDto> getSpeakerDetails(@PathVariable Long speakerId)
    {
        SpeakerDetailResponseDto speakers = speakerService.getSpeakerDetails(speakerId);

        return ApiResponse.onSuccess("연사 상세정보 조회에 성공했습니다.",speakers);
    }



}
