package com.hongik.devtalk.domain.seminar;

import com.hongik.devtalk.domain.live.LiveBaseError;
import com.hongik.devtalk.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomSeminarApplicantErrorCode implements BaseErrorCode {
    SEMINAR_APPLICANT_ERROR(HttpStatus.NO_CONTENT,"APPLICANT_4001","세미나 신청기간이 아닙니다."),
    SESSION_NOT_FOUND(HttpStatus.NOT_FOUND,"SESSION_4041","세션을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

}
