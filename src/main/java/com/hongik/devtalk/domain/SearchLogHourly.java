package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "search_log_hourly",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_target_browser_keyword_hour",
                columnNames = {"target_type","browser_id","keyword_norm","hour_bucket"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchLogHourly {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="target_type", nullable=false, length=16)
    private String targetType; // SEMINAR / SPEAKER

    @Column(name="browser_id", nullable=false, length=64)
    private String browserId;

    @Column(name="keyword_norm", nullable=false, length=100)
    private String keywordNorm;

    @Column(name="hour_bucket", nullable=false)
    private LocalDateTime hourBucket;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @Builder
    private SearchLogHourly(String targetType, String browserId, String keywordNorm, LocalDateTime hourBucket, LocalDateTime createdAt) {
        this.targetType = targetType;
        this.browserId = browserId;
        this.keywordNorm = keywordNorm;
        this.hourBucket = hourBucket;
        this.createdAt = createdAt;
    }

    public static SearchLogHourly of(String targetType, String browserId, String keywordNorm, LocalDateTime hourBucket) {
        return SearchLogHourly.builder()
                .targetType(targetType)
                .browserId(browserId)
                .keywordNorm(keywordNorm)
                .hourBucket(hourBucket)
                .createdAt(LocalDateTime.now())
                .build();
    }
}