package com.hongik.devtalk.controller.mail;

import com.hongik.devtalk.domain.enums.MailType;
import com.hongik.devtalk.domain.mail.dto.EmailOnlyDTO;
import com.hongik.devtalk.domain.mail.dto.MailHtmlSendDTO;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mail")
@Tag(name="SendMail",description = "메일 전송 관련 API")
public class MailConfirmController {

    @PostMapping("/send")
    @Operation(summary = "사용자가 신청폼을 제출하면, 신청확인 이메일 전송")
    public ResponseEntity<ApiResponse<MailHtmlSendDTO>> sendConfirmation(@RequestBody EmailOnlyDTO request){
        MailHtmlSendDTO result = new MailHtmlSendDTO(
                request.getEmailAddr(),
                "[DevTalk 신청 완료] 세미나 등록이 완료되었습니다.",
                "<h1>DevTalk 신청 완료!</h1><p>성공적으로 세미나에 등록되었습니다.</p>",
                MailType.SEMINAR_CONFIRMATION
        );
        return ResponseEntity.ok(ApiResponse.onSuccess("", result));
    }

}
