package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.repository.seminar.SearchKeywordDailyRepository;
import com.hongik.devtalk.repository.seminar.SearchLogHourlyRepository;
import com.hongik.devtalk.repository.seminar.SeminarViewDailyRepository;
import com.hongik.devtalk.repository.seminar.SeminarViewLogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({SeminarViewStatsService.class, SearchStatsService.class, StatsLogInsertTxService.class})
class StatsConcurrencyIntegrationTest {

    @Autowired
    private SeminarViewStatsService seminarViewStatsService;
    @Autowired
    private SearchStatsService searchStatsService;
    @Autowired
    private SeminarViewLogRepository seminarViewLogRepository;
    @Autowired
    private SearchLogHourlyRepository searchLogHourlyRepository;

    @MockBean
    private SeminarViewDailyRepository seminarViewDailyRepository;
    @MockBean
    private SearchKeywordDailyRepository searchKeywordDailyRepository;

    @BeforeEach
    void setup() {
        searchLogHourlyRepository.deleteAll();
        seminarViewLogRepository.deleteAll();

        when(seminarViewDailyRepository.upsertIncrement(any(Long.class), any()))
                .thenReturn(1);
        when(searchKeywordDailyRepository.upsertIncrement(any(), any(), any()))
                .thenReturn(1);
    }

    @AfterEach
    void resetMocks() {
        reset(seminarViewDailyRepository, searchKeywordDailyRepository);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void seminarView_sameBrowserConcurrentRequests_countedOncePerDay() throws Exception {
        Long seminarId = 101L;
        String browserId = "same-browser";
        int concurrency = 32;

        runConcurrently(concurrency, () -> seminarViewStatsService.recordSeminarView(seminarId, browserId));

        assertThat(seminarViewLogRepository.count()).isEqualTo(1);
        verify(seminarViewDailyRepository, times(1)).upsertIncrement(eq(seminarId), any());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void seminarView_differentBrowsersConcurrentRequests_allCounted() throws Exception {
        Long seminarId = 102L;
        int concurrency = 24;

        runConcurrently(concurrency, index ->
                seminarViewStatsService.recordSeminarView(seminarId, "browser-" + index));

        assertThat(seminarViewLogRepository.count()).isEqualTo(concurrency);
        verify(seminarViewDailyRepository, times(concurrency)).upsertIncrement(eq(seminarId), any());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void search_sameBrowserSameKeywordConcurrentRequests_countedOncePerHour() throws Exception {
        String browserId = "same-browser";
        int concurrency = 32;

        runConcurrently(concurrency, () ->
                searchStatsService.recordSearch(SearchStatsService.TARGET_SEMINAR, "  Java   Spring  ", browserId));

        assertThat(searchLogHourlyRepository.count()).isEqualTo(1);
        verify(searchKeywordDailyRepository, times(1))
                .upsertIncrement(eq(SearchStatsService.TARGET_SEMINAR), eq("java spring"), any());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void search_differentBrowsersSameKeywordConcurrentRequests_allCounted() throws Exception {
        int concurrency = 24;

        runConcurrently(concurrency, index ->
                searchStatsService.recordSearch(SearchStatsService.TARGET_SPEAKER, "Kotlin", "browser-" + index));

        assertThat(searchLogHourlyRepository.count()).isEqualTo(concurrency);
        verify(searchKeywordDailyRepository, times(concurrency))
                .upsertIncrement(eq(SearchStatsService.TARGET_SPEAKER), eq("kotlin"), any());
    }

    private void runConcurrently(int concurrency, ThrowingRunnable action) throws Exception {
        runConcurrently(concurrency, index -> action.run());
    }

    private void runConcurrently(int concurrency, IndexedThrowingRunnable action) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(concurrency);
        CountDownLatch ready = new CountDownLatch(concurrency);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(concurrency);
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < concurrency; i++) {
            final int index = i;
            futures.add(executor.submit(() -> {
                ready.countDown();
                try {
                    start.await();
                    action.run(index);
                } catch (Throwable t) {
                    errorRef.compareAndSet(null, t);
                } finally {
                    done.countDown();
                }
            }));
        }

        assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
        start.countDown();
        assertThat(done.await(20, TimeUnit.SECONDS)).isTrue();

        for (Future<?> future : futures) {
            future.get(1, TimeUnit.SECONDS);
        }
        executor.shutdownNow();

        if (errorRef.get() != null) {
            throw new AssertionError("Concurrent task failed", errorRef.get());
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    @FunctionalInterface
    private interface IndexedThrowingRunnable {
        void run(int index) throws Exception;
    }
}
