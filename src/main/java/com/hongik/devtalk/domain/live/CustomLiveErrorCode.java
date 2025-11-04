package com.hongik.devtalk.domain.live;

import com.hongik.devtalk.global.apiPayload.code.BaseErrorCode;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CustomLiveErrorCode implements BaseErrorCode {
    SEMINAR_TIME_ERROR(HttpStatus.NOT_FOUND,"SEMINAR_4041","세미나 진행 및 리뷰작성 기간이 아닙니다"),
    LIVE_URL_NOT_FOUND(HttpStatus.NOT_FOUND,"SEMINARLIVE_4041","세미나 라이브 URL이 아직 등록되지 않았습니다"),
    REVIEW_PERIOD_INVALID(HttpStatus.NOT_FOUND,"REVIEW_4041","리뷰 작성 기간이 아닙니다."),
    ALREADY_ATTEND(HttpStatus.MULTI_STATUS,"ATTEND_4000","이미 출석체크 되었습니다."),
    ATTENDANCE_NOT_YET_OPEN(HttpStatus.NOT_FOUND,"ATTEND_4041","출석체크 기간이 아닙니다."),
    STUDENT_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDENT_4041", "해당 학생을 찾을 수 없습니다."),
    APPLICANT_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLICANT_4041", "해당 학생의 신청 정보를 찾을 수 없습니다."),
    REVIEW_DUPLICATE_ERROR(HttpStatus.MULTI_STATUS,"REVIEW_4091","리뷰는 한 번만 작성 가능합니다."),
    SEMINAR_NOT_FOUND(HttpStatus.NOT_FOUND, "SEMINAR_4041","세미나를 찾을 수 없습니다."),
    ATTEND_ABSENT(HttpStatus.NOT_FOUND,"ATTEND_4091","세미나에 출석한 사용자만 리뷰작성이 가능합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
