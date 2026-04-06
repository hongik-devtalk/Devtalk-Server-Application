package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.SearchLogHourly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface SearchLogHourlyRepository extends JpaRepository<SearchLogHourly, Long> {

    @Modifying
    @Query(value = """
        INSERT INTO search_log_hourly (target_type, browser_id, keyword_norm, hour_bucket, created_at)
        VALUES (:targetType, :browserId, :keywordNorm, :hourBucket, :createdAt)
        ON DUPLICATE KEY UPDATE created_at = created_at
        """, nativeQuery = true)
    int insertIfAbsent(@Param("targetType") String targetType,
                       @Param("browserId") String browserId,
                       @Param("keywordNorm") String keywordNorm,
                       @Param("hourBucket") LocalDateTime hourBucket,
                       @Param("createdAt") LocalDateTime createdAt);
}
