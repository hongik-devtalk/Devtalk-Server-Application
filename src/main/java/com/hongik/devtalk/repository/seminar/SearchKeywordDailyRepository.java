package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.SearchKeywordDaily;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface SearchKeywordDailyRepository
        extends JpaRepository<SearchKeywordDaily, SearchKeywordDaily.SearchKeywordDailyId> {

    // 인기 검색어 TOP5용 쿼리
    @Query("""
        SELECT d.id.keywordNorm, SUM(d.searchCount)
        FROM SearchKeywordDaily d
        WHERE (:target = 'ALL' OR d.id.targetType = :target)
        AND d.id.searchDate BETWEEN :from AND :to
        GROUP BY d.id.keywordNorm
        ORDER BY SUM(d.searchCount) DESC
    """)
    List<Object[]> findTopKeywords(@Param("target") String target,
                                   @Param("from") LocalDate from,
                                   @Param("to") LocalDate to);

    // 동시성 문제 해결용 upsert
    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO search_keyword_daily (target_type, keyword_norm, search_date, search_count)
        VALUES (:targetType, :keywordNorm, :date, 1)
        ON DUPLICATE KEY UPDATE search_count = search_count + 1
        """, nativeQuery = true)
    int upsertIncrement(@Param("targetType") String targetType,
                        @Param("keywordNorm") String keywordNorm,
                        @Param("date") LocalDate date);
}