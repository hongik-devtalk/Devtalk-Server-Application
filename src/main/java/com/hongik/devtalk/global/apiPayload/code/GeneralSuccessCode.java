package com.hongik.devtalk.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralSuccessCode implements BaseSuccessCode {

    OK(HttpStatus.OK, "COMMON2000", "성공적으로 처리했습니다."),
    DELETED(HttpStatus.OK, "COMMON2001", "성공적으로 삭제했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
