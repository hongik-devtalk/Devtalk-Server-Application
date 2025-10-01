package com.hongik.devtalk.scheduler;


import com.hongik.devtalk.domain.enums.MailType;
import com.hongik.devtalk.domain.mail.dto.MailHtmlSendDTO;
import com.hongik.devtalk.service.mail.MailSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeminarReminderScheduler {
    private final MailSendService mailSendService;

    // 세미나 진행 전날 10시에
    // TODO: 시간 반영 안했음 - 아무거나 써둔 것
    @Scheduled(cron = "0 0 10 * * *", zone = "Asia/Seoul")
    public void sendSeminarReminders() {
        log.info("세미나 리마인드 스케줄러 실행");

        // TODO: 내일 열릴 세미나에 대한 신청자 이메일 목록 조회
        // TODO: 관련 service 클래스 만들어야함
        //List<String> emails = seminarService.getTomorrowSeminarEmails();

        // 예시 이메일 반복문
//        for (String email : emails) {
//            MailHtmlSendDTO dto = new MailHtmlSendDTO(
//                    email,
//                    "[DevTalk 리마인드] 세미나가 내일입니다!",
//                    "<h1>세미나 리마인드</h1><p>내일 참석 예정인 세미나를 잊지 말고 확인해주세요!</p>",
//                    MailType.SEMINAR_REMINDER
//            );
//            mailSendService.sendHtmlEmail(dto);
//        }
    }
}
