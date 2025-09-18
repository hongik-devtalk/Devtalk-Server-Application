package com.hongik.devtalk.global.apiPayload.code;

import org.springframework.http.HttpStatus;

public interface BaseErrorCode {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
