package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.SearchLogHourly;
import com.hongik.devtalk.domain.SeminarViewLog;
import com.hongik.devtalk.repository.seminar.SearchLogHourlyRepository;
import com.hongik.devtalk.repository.seminar.SeminarViewLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsLogInsertTxServiceTest {

    @Mock
    private SeminarViewLogRepository seminarViewLogRepository;

    @Mock
    private SearchLogHourlyRepository searchLogHourlyRepository;

    @InjectMocks
    private StatsLogInsertTxService statsLogInsertTxService;

    @Test
    void insertSeminarViewLogIfAbsent_returnsFalseWhenDuplicateWasIgnored() {
        SeminarViewLog log = SeminarViewLog.of(1L, "browser", LocalDate.now());
        when(seminarViewLogRepository.insertIfAbsent(
                log.getSeminarId(), log.getBrowserId(), log.getViewDate(), log.getCreatedAt()))
                .thenReturn(0);

        boolean inserted = statsLogInsertTxService.insertSeminarViewLogIfAbsent(log);

        assertThat(inserted).isFalse();
    }

    @Test
    void insertSearchLogIfAbsent_rethrowsNonDuplicateIntegrityError() {
        SearchLogHourly log = SearchLogHourly.of("SEMINAR", "browser", "spring", LocalDateTime.now());
        DataIntegrityViolationException exception = new DataIntegrityViolationException("non-duplicate");
        when(searchLogHourlyRepository.insertIfAbsent(
                log.getTargetType(), log.getBrowserId(), log.getKeywordNorm(), log.getHourBucket(), log.getCreatedAt()))
                .thenThrow(exception);

        assertThatThrownBy(() -> statsLogInsertTxService.insertSearchLogIfAbsent(log))
                .isSameAs(exception);
    }
}
