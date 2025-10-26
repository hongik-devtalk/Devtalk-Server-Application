package com.hongik.devtalk.scheduler;


import com.hongik.devtalk.service.mail.D1ReminderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeminarReminderScheduler {
    //private final MailSendService mailSendService;
    private final D1ReminderService d1ReminderService;

    // 세미나 진행 전날 10시에
    // 매일 10:00 KST
    @Scheduled(cron = "0 0 10 * * *", zone = "Asia/Seoul")
    public void sendSeminarReminders() {
        log.info("세미나 리마인드 스케줄러 실행");
        int sent = d1ReminderService.sendD1Reminders();
        log.info("세미나 리마인드 전송 완료: {}건", sent);

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
