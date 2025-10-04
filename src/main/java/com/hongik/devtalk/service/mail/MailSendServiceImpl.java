package com.hongik.devtalk.service.mail;

import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.enums.ParticipationType;
import com.hongik.devtalk.util.S3MailTemplateLoader;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSendServiceImpl implements MailSendService{
    private final JavaMailSender javaMailSender;
    private final S3MailTemplateLoader mailTemplateLoader;

    @Value("${mail.templates.confirmation}")
    private String confirmationTemplateUrl;

    @Value("${mail.templates.reminder.offline}") // TODO: 리마인더
    private String reminderOfflineTemplateUrl;

    @Value("${mail.templates.reminder.online}") // TODO: 리마인더
    private String reminderOnlineTemplateUrl;

    @Value("${mail.from.address}")
    private String fromAddress;

    @Override
    public boolean sendConfirmationMail(String email, ParticipationType type, String name, Seminar seminar){
        String subject = "[DevTalk]\u00A0" + seminar.getSeminarNum() +"회차 세미나 신청 완료";
        String htmlContent = mailTemplateLoader.loadTemplate(confirmationTemplateUrl);

        if(htmlContent == null){
            log.error("템플릿 로딩 실패 - URL: {}",confirmationTemplateUrl);
            return false;
        }
        // 세미나 일시 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 (E) a h시 mm분")
                .withLocale(Locale.KOREA);
        htmlContent = htmlContent
                .replace("{N}", String.valueOf(seminar.getSeminarNum()))
                .replace("{참석자 이름}", name)
                .replace("{세미나 일시}", seminar.getSeminarDate().format(formatter))
                .replace("{세미나 장소}", seminar.getPlace())
                .replace("{문의 링크}", "http://pf.kakao.com/_Gxbrwn");
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(htmlContent,true);

            javaMailSender.send(message);
            log.info("세미나 신청 확인 메일 전송 성공: {}",email);
            return true;
        }catch(Exception e){
            log.error("세미나 신청 확인 메일 전송 실패 - 수신자: {}, 에러: {}", email, e.getMessage(),e);
            return false;
            }
        }
    }
