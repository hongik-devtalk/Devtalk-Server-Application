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

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean insertSeminarViewLogIfAbsent(SeminarViewLog log) {
        return seminarViewLogRepository.insertIfAbsent(
                log.getSeminarId(),
                log.getBrowserId(),
                log.getViewDate(),
                log.getCreatedAt()
        ) > 0;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean insertSearchLogIfAbsent(SearchLogHourly log) {
        return searchLogHourlyRepository.insertIfAbsent(
                log.getTargetType(),
                log.getBrowserId(),
                log.getKeywordNorm(),
                log.getHourBucket(),
                log.getCreatedAt()
        ) > 0;
    }
}
