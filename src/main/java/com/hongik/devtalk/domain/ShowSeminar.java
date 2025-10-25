package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShowSeminar { // single row entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id", nullable = true)
    private Seminar seminar;

    // 신청 활성화
    private boolean applicantActivate;

    // Live 활성화
    private boolean LiveActivate;

    // 가장 최근에 업데이트 된 정보를 기준으로 조회하기
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

}
