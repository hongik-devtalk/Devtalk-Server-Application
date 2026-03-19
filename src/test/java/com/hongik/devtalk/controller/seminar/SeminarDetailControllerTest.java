package com.hongik.devtalk.controller.seminar;

import com.hongik.devtalk.domain.seminar.detail.dto.SeminarDetailResponseDto;
import com.hongik.devtalk.domain.seminar.detail.dto.SeminarSearchResponseDto;
import com.hongik.devtalk.global.apiPayload.exception.handler.ExceptionAdvice;
import com.hongik.devtalk.service.seminar.SearchStatsService;
import com.hongik.devtalk.service.seminar.SeminarDetailService;
import com.hongik.devtalk.service.seminar.SeminarViewStatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SeminarDetailControllerTest {

    @Mock
    private SeminarDetailService seminarDetailService;

    @Mock
    private SeminarViewStatsService seminarViewStatsService;

    @Mock
    private SearchStatsService searchStatsService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(
                        new SeminarDetailController(seminarDetailService, seminarViewStatsService, searchStatsService))
                .setControllerAdvice(new ExceptionAdvice())
                .build();
    }

    @Test
    void getSeminars_returnsSuccessWhenStatsRecordingFails() throws Exception {
        when(seminarDetailService.getSeminarDetail(1L))
                .thenReturn(SeminarDetailResponseDto.builder().topic("Seminar").build());
        doThrow(new RuntimeException("stats failed"))
                .when(seminarViewStatsService)
                .recordSeminarView(1L, "browser-1");

        mockMvc.perform(get("/user/seminars/1")
                        .header("X-Client-Id", "browser-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true));

        verify(seminarDetailService).getSeminarDetail(1L);
        verify(seminarViewStatsService).recordSeminarView(1L, "browser-1");
    }

    @Test
    void searchSeminars_callsSearchBeforeBestEffortStatsRecording() throws Exception {
        when(seminarDetailService.searchSeminars("java"))
                .thenReturn(List.of(SeminarSearchResponseDto.builder().topic("Java").build()));
        doThrow(new RuntimeException("stats failed"))
                .when(searchStatsService)
                .recordSearch(SearchStatsService.TARGET_SEMINAR, "java", "browser-1");

        mockMvc.perform(get("/user/seminars/search")
                        .queryParam("keyword", "java")
                        .header("X-Client-Id", "browser-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true));

        InOrder inOrder = inOrder(seminarDetailService, searchStatsService);
        inOrder.verify(seminarDetailService).searchSeminars("java");
        inOrder.verify(searchStatsService).recordSearch(SearchStatsService.TARGET_SEMINAR, "java", "browser-1");
    }
}
