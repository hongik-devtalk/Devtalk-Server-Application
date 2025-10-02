package com.hongik.devtalk.global.apiPayload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GeneralErrorCode implements BaseErrorCode {

    // 인증 에러
    DUPLICATE_LOGINID(HttpStatus.BAD_REQUEST,"AUTH_4001","중복되는 아이디가 존재합니다."),
    MISSING_AUTH_INFO(HttpStatus.UNAUTHORIZED, "AUTH_4011", "인증 정보가 누락되었습니다."),
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "AUTH_4012", "올바르지 않은 아이디, 혹은 비밀번호입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_4012", "유효하지 않은 토큰입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH_4031", "접근 권한이 없습니다."),
    TOKEN_EXPIRED(HttpStatus.valueOf(419), "AUTH_4191", "토큰이 만료되었습니다."),

    // 유저 에러
    SEMINARINFO_NOT_FOUND(HttpStatus.NOT_FOUND,"SEMINARINFO_4041","seminarId를 찾을 수 없습니다."),
    
    // 이미지 에러
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "IMAGE_4041", "해당 타입의 이미지를 찾을 수 없습니다."),
    
    // 링크 에러
    INQUIRY_LINK_NOT_FOUND(HttpStatus.NOT_FOUND, "LINK_4041", "문의하기 링크를 찾을 수 없습니다."),

    // 후기 에러
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW_4041", "존재하지 않는 후기입니다."),

    // 요청/파라미터 에러
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "REQ_4001", "필수 파라미터가 누락되었습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "REQ_4002", "파라미터 형식이 잘못되었습니다."),
    UNSUPPORTED_CONTENT_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "REQ_4151", "지원하지 않는 Content-Type입니다."),

    // 관리자 에러
    ADMIN_NOT_FOUND(HttpStatus.NOT_FOUND, "ADMIN_4041", "관리자 정보가 없습니다."),

    // API/라우팅 에러
    API_NOT_FOUND(HttpStatus.NOT_FOUND, "API_4041", "존재하지 않는 API입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "API_4051", "지원하지 않는 HTTP 메서드입니다."),

    // 파일 업로드 에러
    FILE_EMPTY(HttpStatus.BAD_REQUEST, "FILE_4001", "파일이 비어있습니다."),
    UNSUPPORTED_FILE_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "FILE_4151", "지원하지 않는 파일 형식입니다."),
    FILE_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "FILE_4131", "파일 크기가 너무 큽니다."),
    
    // S3 관련 에러
    S3_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5001", "S3 파일 업로드에 실패했습니다."),
    S3_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5002", "S3 파일 삭제에 실패했습니다."),
    S3_CONNECTION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5003", "S3 서비스 연결에 실패했습니다."),

    // 서버 내부 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_5001", "서버 내부 오류입니다."),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "SERVER_5031", "서버가 일시적으로 불안정합니다."),
    EXTERNAL_SERVICE_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "SERVER_5041", "외부 서비스 응답 지연"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
