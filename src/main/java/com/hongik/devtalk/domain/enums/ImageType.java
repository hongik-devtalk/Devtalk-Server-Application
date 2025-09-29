package com.hongik.devtalk.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {
    INTRO("INTRO", "Devtalk 소개 이미지"),
    PREVIOUS_SEMINAR("PREVIOUS_SEMINAR", "이전 세미나 보러가기 이미지");

    private final String code;
    private final String description;
}
