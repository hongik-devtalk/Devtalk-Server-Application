package com.hongik.devtalk.service.mail;

import com.hongik.devtalk.domain.mail.dto.MailHtmlSendDTO;

public interface MailSendService {
    void sendHtmlEmail(MailHtmlSendDTO dto);
}
