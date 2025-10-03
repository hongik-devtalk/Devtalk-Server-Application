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
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Applicant", description = "세미나 신청 API")
@RequestMapping("/user/seminars")
public class SeminarApplicantController {
    private final SeminarApplicantService seminarApplicantService;

    @PostMapping
    @Operation(summary = "세미나 신청 API", description = "세미나에 대한 신청 정보를 받아 처리합니다. 학생이 존재하지 않으면 새로 생성하고, 존재하는 경우 정보를 업데이트합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "세미나 신청에 필요한 정보를 담은 DTO",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ApplicantRequestDto.class),
                    examples = @ExampleObject(
                            name = "세미나 신청 요청 예시",
                            value = """
                                {
                                  "studentNum": "C012345",
                                  "name": "김데브",
                                  "grade": 3,
                                  "gradeEtc": null,
                                  "email": "devtalk123@example.com",
                                  "phone": "010-1111-2222",
                                  "departments": ["컴퓨터공학과"],
                                  "departmentEtc": "AI융합학과",
                                  "participationType": "OFFLINE",
                                  "inflowPath": "INSTAGRAM",
                                  "inflowPathEtc": null,
                                  "questions": [
                                    {
                                      "sessionId": 1,
                                      "content": "첫 번째 세션의 기술을 실제 프로젝트에 적용할 때 주의할 점이 있을까요?"
                                    },
                                    {
                                      "sessionId": 2,
                                      "content": "두 번째 세션 발표자님께 질문 있습니다. 관련 분야로 취업하기 위해 가장 중요하게 준비해야 할 역량은 무엇이라고 생각하시나요?"
                                    }
                                  ]
                                }"""
                    )
            )
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200", description = "OK, 성공적으로 신청 완료",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "성공 응답 예시",
                                    value = "{\"isSuccess\": true, \"code\": \"COMMON2000\", \"message\": \"성공적으로 신청이 완료되었습니다.\", \"result\": {\"studentId\": 2, \"seminarId\": 1, \"applicantId\": 2}, \"error\": null}"
                            ))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "207", description = "ALREADY_APPLIED_SEMINAR: 이미 지원한 세미나",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "중복 지원 예시",
                                    value = "{\"isSuccess\": false, \"code\": \"APPLICANT_4002\", \"message\": \"이미 신청한 세미나입니다.\", \"result\": null}"
                            ))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204", description = "SEMINAR_APPLICANT_ERROR: 세미나 신청 기간이 아님",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "신청 기간 오류 예시",
                                    value = "{\"isSuccess\": false, \"code\": \"APPLICANT_4001\", \"message\": \"세미나 신청기간이 아닙니다.\", \"result\": null}"
                            ))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400", description = "Bad Request: 유효성 검사 실패 (필수 파라미터 누락 등)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class),
                            examples = @ExampleObject(
                                    name = "파라미터 누락 예시",
                                    value = "{\"isSuccess\": false, \"code\": \"REQ_4001\", \"message\": \"필수 파라미터가 누락되었습니다.\", \"result\": null}"
                            )))
    })
    public ApiResponse<ApplicantResponseDto> createApplicant(@RequestBody ApplicantRequestDto applicantRequestDto) {
        return seminarApplicantService.createApplicant(applicantRequestDto);
    }
}