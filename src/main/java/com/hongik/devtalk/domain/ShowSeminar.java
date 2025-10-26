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
    private boolean liveActivate;

    // 업데이트 될 때마다 새로 찍힘
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public void update(Seminar seminar, boolean applicantActivate, boolean liveActivate) {
        this.seminar = seminar;
        this.applicantActivate = applicantActivate;
        this.liveActivate = liveActivate;
    }
}
