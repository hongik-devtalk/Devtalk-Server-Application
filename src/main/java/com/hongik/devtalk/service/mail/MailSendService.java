package com.hongik.devtalk.service.mail;

import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.enums.ParticipationType;

public interface MailSendService {
    boolean sendConfirmationMail(String email, ParticipationType type, String name, Seminar seminar);
}
