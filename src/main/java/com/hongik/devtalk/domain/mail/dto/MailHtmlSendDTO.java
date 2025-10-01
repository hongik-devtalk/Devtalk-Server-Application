package com.hongik.devtalk.domain.mail.dto;

import com.hongik.devtalk.domain.enums.MailType;
import lombok.Getter;

@Getter
public class MailHtmlSendDTO {
    private String emailAddr; // 수신자 이메일
    private String title;
    private String content;
    private MailType mailType;


    public MailHtmlSendDTO(String emailAddr, String title, String content, MailType mailType) {
        this.emailAddr = emailAddr;
        this.title = title;
        this.content = content;
        this.mailType = mailType;
    }
}
