package com.hongik.devtalk.controller.speaker;

import com.hongik.devtalk.domain.speaker.dto.SpeakerSearchResponseDto;
import com.hongik.devtalk.global.apiPayload.exception.handler.ExceptionAdvice;
import com.hongik.devtalk.service.seminar.SearchStatsService;
import com.hongik.devtalk.service.speaker.SpeakerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SpeakerControllerTest {

    @Mock
    private SpeakerService speakerService;

    @Mock
    private SearchStatsService searchStatsService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SpeakerController(speakerService, searchStatsService))
                .setControllerAdvice(new ExceptionAdvice())
                .build();
    }

    @Test
    void searchSpeakers_returnsBadRequestForBlankKeyword() throws Exception {
        mockMvc.perform(get("/user/speakers/search")
                        .queryParam("keyword", "   ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("REQ_4002"));

        verify(speakerService, never()).searchSpeakers(anyString());
        verify(searchStatsService, never()).recordSearch(anyString(), anyString(), anyString());
    }

    @Test
    void searchSpeakers_returnsSuccessWhenStatsRecordingFails() throws Exception {
        when(speakerService.searchSpeakers("spring"))
                .thenReturn(List.of(SpeakerSearchResponseDto.builder().speakerName("Alice").build()));
        doThrow(new RuntimeException("stats failed"))
                .when(searchStatsService)
                .recordSearch(SearchStatsService.TARGET_SPEAKER, "spring", "browser-1");

        mockMvc.perform(get("/user/speakers/search")
                        .queryParam("keyword", "spring")
                        .header("X-Client-Id", "browser-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true));

        verify(speakerService).searchSpeakers("spring");
        verify(searchStatsService).recordSearch(SearchStatsService.TARGET_SPEAKER, "spring", "browser-1");
    }
}
