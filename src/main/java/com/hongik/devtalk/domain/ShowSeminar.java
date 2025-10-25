package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShowSeminar { // 홈화면 노출 세미나
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long showSeminarNum;

    // 신청 활성화
    private boolean applicantActivate;

    // Live 활성화
    private boolean LiveActivate;

}
