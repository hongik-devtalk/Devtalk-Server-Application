package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Live {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "live_id")
    private Long id;

    // 세미나별로 라이브링크 제공
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id", nullable = false, unique = true)
    private Seminar seminar;

    @Column(length = 2048, nullable = false)
    private String liveUrl;
}
