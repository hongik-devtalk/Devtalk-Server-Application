package com.hongik.devtalk.domain.live;

import com.hongik.devtalk.domain.live.LiveBaseError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LiveError implements LiveBaseError {
    STUDENT_NOT_FOUND("학생을 찾을 수 없습니다.","학번과 이름을 확인하고 입력해주세요."),
    SEMINAR_NOT_FOUND("세미나 입장 기한이 아니거나 리뷰 작성 기한이 만료되었습니다.","세미나 입장은 당일부터, 리뷰 작성 기한은 세미나 이후 10일까지 입니다."),
    APPLICANT_NOT_FOUND("신청 정보를 찾을 수 없습니다.","세미나 신청 여부를 확인해주세요."),
    LIVEURL_NOT_FOUND("세미나 라이브 url을 찾을 수 없습니다.","라이브 url이 등록되었는지 확인해주세요."),
    REVIEW_PERIOD_INVALID("리뷰 등록 기간이 아닙니다.", "리뷰 등록 기간을 확인해주세요"),
    ATTENDANCE_NOT_YET_OPEN("출석체크 기간이 아닙니다","출석체크는 세미나 시작 10분전부터 가능합니다.");

    private final String reason;
    private final String hint;
}