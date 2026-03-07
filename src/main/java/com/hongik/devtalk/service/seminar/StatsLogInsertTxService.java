package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.SearchLogHourly;
import com.hongik.devtalk.domain.SeminarViewLog;
import com.hongik.devtalk.repository.seminar.SearchLogHourlyRepository;
import com.hongik.devtalk.repository.seminar.SeminarViewLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatsLogInsertTxService {

    private final SeminarViewLogRepository seminarViewLogRepository;
    private final SearchLogHourlyRepository searchLogHourlyRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertSeminarViewLog(SeminarViewLog log) {
        seminarViewLogRepository.saveAndFlush(log);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertSearchLog(SearchLogHourly log) {
        searchLogHourlyRepository.saveAndFlush(log);
    }
}
