package com.hongik.devtalk.controller;

import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.enums.ParticipationType;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.global.apiPayload.code.BaseErrorCode;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import com.hongik.devtalk.service.mail.MailSendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/api/mail-test")
@RequiredArgsConstructor
public class MailTestController {

    private final MailSendService mailSendService;
    private final SeminarRepository seminarRepository;

    @PostMapping
    public ApiResponse<String> sendTestMail(@RequestParam String email, String name) {
        Seminar latestSeminar = seminarRepository.findAllByOrderBySeminarNumDesc()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("세미나가 존재하지 않습니다."));

        boolean result = mailSendService.sendConfirmationMail(
                email,
                ParticipationType.ONLINE,
                name,
                latestSeminar
        );

        if (result) {
            return ApiResponse.onSuccess("메일 발송 성공");
        } else {
            return ApiResponse.onFailure(INTERNAL_SERVER_ERROR,"실패");
        }
    }
}