package com.hongik.devtalk.service.mail;

import com.hongik.devtalk.domain.mail.dto.MailHtmlSendDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSendServiceImpl implements MailSendService{
    private final JavaMailSender mailSender;
    @Override
    public void sendHtmlEmail(MailHtmlSendDTO dto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // true = multipart
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(dto.getEmailAddr()); // 수신자
            helper.setSubject(dto.getTitle()); // 제목
            helper.setText(dto.getContent(), true); // true = HTML

            // Optional: 발신자 지정
            helper.setFrom("dev.hongik@gmail.com", "홍익 DevTalk");

            mailSender.send(message);

            log.info("HTML 이메일 전송 완료: to={}, subject={}", dto.getEmailAddr(), dto.getTitle());

        } catch (MessagingException e) {
            log.error("이메일 전송 실패", e);
            throw new RuntimeException("메일 전송 실패", e);
        } catch (Exception e) {
            log.error("메일 전송 중 예외 발생", e);
            throw new RuntimeException("메일 전송 중 오류", e);
        }
    }

}
