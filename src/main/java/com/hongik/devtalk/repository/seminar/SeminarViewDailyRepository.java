package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.SeminarViewDaily;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface SeminarViewDailyRepository
        extends JpaRepository<SeminarViewDaily, SeminarViewDaily.SeminarViewDailyId> {

    // 그래프 조회용 (service에서 사용중인 메서드)
    List<SeminarViewDaily> findByIdSeminarIdAndIdViewDateBetween(Long seminarId, LocalDate from, LocalDate to);

    // 동시성 해결용 upsert
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO seminar_view_daily (seminar_id, view_date, view_count)
        VALUES (:seminarId, :viewDate, 1)
        ON DUPLICATE KEY UPDATE view_count = view_count + 1
        """, nativeQuery = true)
    int upsertIncrement(@Param("seminarId") Long seminarId,
                        @Param("viewDate") LocalDate viewDate);
}