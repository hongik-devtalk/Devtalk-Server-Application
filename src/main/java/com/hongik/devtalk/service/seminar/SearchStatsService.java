package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.SearchLogHourly;
import com.hongik.devtalk.repository.seminar.SearchKeywordDailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SearchStatsService {

    public static final String TARGET_SEMINAR = "SEMINAR";
    public static final String TARGET_SPEAKER = "SPEAKER";
    public static final String TARGET_ALL = "ALL";

    private final StatsLogInsertTxService statsLogInsertTxService;
    private final SearchKeywordDailyRepository dailyRepo;

    public void recordSearch(String targetType, String rawKeyword, String browserId) {
        String keywordNorm = normalize(rawKeyword);
        if (keywordNorm.isEmpty()) return;
        if (browserId == null || browserId.isBlank()) return;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hourBucket = now.withMinute(0).withSecond(0).withNano(0);
        LocalDate today = now.toLocalDate();

        // If duplicate for same browser/hour/keyword, skip daily increment.
        try {
            statsLogInsertTxService.insertSearchLog(
                    SearchLogHourly.of(targetType, browserId, keywordNorm, hourBucket)
            );
        } catch (DataIntegrityViolationException dup) {
            return;
        }

        dailyRepo.upsertIncrement(targetType, keywordNorm, today);
    }

    @Transactional(readOnly = true)
    public List<TopKeyword> getTop5(String target, LocalDate from, LocalDate to) {
        List<Object[]> rows = dailyRepo.findTopKeywords(target, from, to);
        List<TopKeyword> result = new ArrayList<>();
        for (int i = 0; i < Math.min(5, rows.size()); i++) {
            Object[] r = rows.get(i);
            String keyword = (String) r[0];
            Long cnt = (Long) r[1];
            result.add(new TopKeyword(keyword, cnt.intValue()));
        }
        return result;
    }

    public record TopKeyword(String keyword, int count) {}

    private String normalize(String s) {
        if (s == null) return "";
        String t = s.trim();
        if (t.isEmpty()) return "";
        t = t.replaceAll("\\s+", " ");
        t = t.toLowerCase();
        return t;
    }
}
