package com.hongik.devtalk.global.apiPayload.exception.handler;

import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.global.apiPayload.code.BaseErrorCode;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionAdvice {

    @ExceptionHandler(GeneralException.class)

    @ExceptionHandler(MethodArgumentNotValidException.class)

    @ExceptionHandler(MissingServletRequestParameterException.class)

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)

    @ExceptionHandler(HttpMessageNotReadableException.class)

    @ExceptionHandler(Exception.class)
}
