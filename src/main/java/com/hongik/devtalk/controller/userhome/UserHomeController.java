package com.hongik.devtalk.controller.userhome;

import com.hongik.devtalk.domain.Speaker;
import com.hongik.devtalk.domain.userhome.dto.ReviewListResponseDTO;
import com.hongik.devtalk.domain.userhome.dto.SeminarInfoResponseDTO;
import com.hongik.devtalk.domain.userhome.dto.SpeakerListResponseDTO;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.userhome.SeminarInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name="[User] MainPage",description = "유저 홈화면 관련 API")
public class UserHomeController {

    private final SeminarInfoService seminarInfoService;

    @GetMapping("/{seminarId}")
    @Operation(summary = "현재 신청받고 있는 세미나 정보 조회")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "세미나 정보 조회 성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "seminarId를 찾을 수 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class),
                    examples = {
                            @ExampleObject(value = "{\"isSuccess\": false, \"code\": \"SEMINARINFO_4041\", \"message\": \"seminarId를 찾을 수 없습니다.\", \"result\": null,\"error\": null}")
                    })
            )
    })
    public ResponseEntity<ApiResponse<SeminarInfoResponseDTO>> getSeminar(@PathVariable Long seminarId) {
        SeminarInfoResponseDTO result = seminarInfoService.getSeminarById(seminarId);
        return ResponseEntity.ok(ApiResponse.onSuccess("세미나 정보 조회 성공", result));
    }

    // 2차 개발 사항
    @GetMapping("/speaker/{seminarId}")
    @Operation(summary = "[2차] 해당 세미나 연사 리스트 조회")
    public ResponseEntity<ApiResponse<SpeakerListResponseDTO>> getSpeakerList(@PathVariable Long seminarId){
        return ResponseEntity.ok().build(); // 임시
    }

    @GetMapping("/review/{seminarId}")
    @Operation(summary = "[2차] 해당 세미나 후기 리스트 조회")
    public ResponseEntity<ApiResponse<ReviewListResponseDTO>> getReviewList(@PathVariable Long seminarId){
        return ResponseEntity.ok().build(); // 임시
    }

}
