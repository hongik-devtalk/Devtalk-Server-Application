package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.SeminarViewLog;
import com.hongik.devtalk.repository.seminar.SeminarViewDailyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SeminarViewStatsService {

    private final StatsLogInsertTxService statsLogInsertTxService;
    private final SeminarViewDailyRepository dailyRepo;

    public void recordSeminarView(Long seminarId, String browserId) {
        if (browserId == null || browserId.isBlank()) return;

        LocalDate today = LocalDate.now();

        // If duplicate for same browser/day, skip daily increment.
        try {
            statsLogInsertTxService.insertSeminarViewLog(SeminarViewLog.of(seminarId, browserId, today));
        } catch (DataIntegrityViolationException dup) {
            return;
        }

        dailyRepo.upsertIncrement(seminarId, today);
    }

    @Transactional(readOnly = true)
    public List<ViewPoint> getDailyGraph(Long seminarId, LocalDate from, LocalDate to) {
        var rows = dailyRepo.findByIdSeminarIdAndIdViewDateBetween(seminarId, from, to);

        Map<LocalDate, Integer> map = new HashMap<>();
        for (var r : rows) map.put(r.getId().getViewDate(), r.getViewCount());

        List<ViewPoint> points = new ArrayList<>();
        for (LocalDate d = from; !d.isAfter(to); d = d.plusDays(1)) {
            points.add(new ViewPoint(d.toString(), map.getOrDefault(d, 0)));
        }
        return points;
    }

    public record ViewPoint(String date, int count) {}
}
