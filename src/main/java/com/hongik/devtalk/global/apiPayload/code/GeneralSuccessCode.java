package com.hongik.devtalk.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralSuccessCode implements BaseSuccessCode {

    OK(HttpStatus.OK, "COMMON2000", "성공적으로 처리했습니다."),

    //auth
    LOGIN_SUCCESS(HttpStatus.OK, "AUTH2000", "로그인에 성공하였습니다."),
    JOIN_SUCCESS(HttpStatus.CREATED,"AUTH2010","가입에 성공하였습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
