package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "seminar_view_log",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_seminar_browser_date",
                columnNames = {"seminar_id", "browser_id", "view_date"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeminarViewLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="seminar_id", nullable=false)
    private Long seminarId;

    @Column(name="browser_id", nullable=false, length=64)
    private String browserId;

    @Column(name="view_date", nullable=false)
    private LocalDate viewDate;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @Builder
    private SeminarViewLog(Long seminarId, String browserId, LocalDate viewDate, LocalDateTime createdAt) {
        this.seminarId = seminarId;
        this.browserId = browserId;
        this.viewDate = viewDate;
        this.createdAt = createdAt;
    }

    public static SeminarViewLog of(Long seminarId, String browserId, LocalDate date) {
        return SeminarViewLog.builder()
                .seminarId(seminarId)
                .browserId(browserId)
                .viewDate(date)
                .createdAt(LocalDateTime.now())
                .build();
    }
}