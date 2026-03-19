package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.SeminarViewLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SeminarViewLogRepository extends JpaRepository<SeminarViewLog, Long> {

    @Modifying
    @Query(value = """
        INSERT INTO seminar_view_log (seminar_id, browser_id, view_date, created_at)
        VALUES (:seminarId, :browserId, :viewDate, :createdAt)
        ON DUPLICATE KEY UPDATE created_at = created_at
        """, nativeQuery = true)
    int insertIfAbsent(@Param("seminarId") Long seminarId,
                       @Param("browserId") String browserId,
                       @Param("viewDate") LocalDate viewDate,
                       @Param("createdAt") LocalDateTime createdAt);
}
