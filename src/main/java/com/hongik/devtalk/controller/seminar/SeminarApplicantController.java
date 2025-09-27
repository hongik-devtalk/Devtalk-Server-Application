package com.hongik.devtalk.controller.seminar;

import com.hongik.devtalk.domain.seminar.dto.ApplicantRequestDto;
import com.hongik.devtalk.domain.seminar.dto.ApplicantResponseDto;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.seminar.SeminarApplicantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Applicant", description = "세미나 신청 API")
@RequestMapping("/user/seminars")
public class SeminarApplicantController {
    private SeminarApplicantService seminarApplicantService;

    @PostMapping
    @Operation(summary = "세미나 신청 API", description = "세미나에 대한 신청 정보를 받아 처리합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "OK, 성공적으로 신청 완료",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "", description = "이미 지원한 세미나",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "중복 지원 예시",
                                    value = "{\"isSuccess\": false, \"code\": \"\", \"message\": \"이미 지원한 세미나입니다.\", \"result\": null, \"error\": {\"reason\": \"Already applied\", \"hint\": \"한 세미나에 한 번만 지원할 수 있습니다.\"}}"
                            ))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "REQ_4001", description = "필수 파라미터 누락",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "파라미터 누락 예시",
                                    value = "{\"isSuccess\": false, \"code\": \"REQ_4001\", \"message\": \"필수 파라미터가 누락되었습니다.\", \"result\": null, \"error\": {\"reason\": \"Missing required fields\", \"hint\": \"학번, 이름, 전화번호는 필수 입력 항목입니다.\"}}"
                            )))
    })
    public ApiResponse<ApplicantResponseDto> createApplicant(@RequestBody ApplicantRequestDto applicantRequestDto) {
        // 임시 더미 데이터로 응답 생성
        ApplicantResponseDto responseDto = ApplicantResponseDto.builder()
                .studentId(10L)
                .seminarId(5L)
                .applicantId(3L)
                .build();

        return ApiResponse.onSuccess("성공적으로 신청이 완료되었습니다.", responseDto);
    }
}
