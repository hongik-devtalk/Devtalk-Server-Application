package com.hongik.devtalk.service;

import com.hongik.devtalk.domain.MainpageImages;
import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.Session;
import com.hongik.devtalk.domain.ShowSeminar;
import com.hongik.devtalk.domain.Speaker;
import com.hongik.devtalk.domain.showseminar.dto.ShowSeminarResponseDTO;
import com.hongik.devtalk.repository.SessionRepository;
import com.hongik.devtalk.repository.mainpage.MainpageImagesRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import com.hongik.devtalk.repository.seminar.ShowSeminarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShowSeminarServiceTest {

    @Mock
    private ShowSeminarRepository showSeminarRepository;
    @Mock
    private SeminarRepository seminarRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private MainpageImagesRepository mainpageImagesRepository;

    @InjectMocks
    private ShowSeminarService showSeminarService;

    @Test
    void getCurrentShowSeminar_returnsMainPosterAndThreeCards() {
        Seminar seminar = Seminar.builder()
                .id(1L)
                .seminarNum(12)
                .topic("DevTalk 12")
                .seminarDate(LocalDateTime.of(2026, 3, 1, 14, 0))
                .place("R801")
                .thumbnailUrl("https://cdn/seminar-thumb.png")
                .build();

        ShowSeminar showSeminar = ShowSeminar.builder()
                .id(1L)
                .seminar(seminar)
                .applicantActivate(true)
                .liveActivate(false)
                .build();

        Session session1 = Session.builder()
                .id(10L)
                .seminar(seminar)
                .speaker(Speaker.builder().id(100L).name("Alice").profileUrl("https://cdn/alice.png").build())
                .title("Part 1")
                .description("desc")
                .partTag("1부")
                .oneLineSummary("요약1")
                .build();

        Session session2 = Session.builder()
                .id(11L)
                .seminar(seminar)
                .speaker(Speaker.builder().id(101L).name("Bob").profileUrl("https://cdn/bob.png").build())
                .title("Part 2")
                .description("desc")
                .partTag("2부")
                .oneLineSummary("요약2")
                .build();

        MainpageImages mainpageImages = MainpageImages.builder()
                .id("mainpage_images_1")
                .introUrl("https://cdn/poster.png")
                .build();

        when(showSeminarRepository.findFirstByOrderByIdAsc()).thenReturn(Optional.of(showSeminar));
        when(mainpageImagesRepository.findTopByOrderById()).thenReturn(Optional.of(mainpageImages));
        when(sessionRepository.findBySeminarIdWithSpeaker(1L)).thenReturn(List.of(session1, session2));

        ShowSeminarResponseDTO response = showSeminarService.getCurrentShowSeminar();

        assertThat(response.getSeminarId()).isEqualTo(1L);
        assertThat(response.getSeminarNum()).isEqualTo(12);
        assertThat(response.getMainPosterImageUrl()).isEqualTo("https://cdn/poster.png");
        assertThat(response.getMainCards().getCard1().getImageUrl()).isEqualTo("https://cdn/seminar-thumb.png");
        assertThat(response.getMainCards().getCard1().getSchedule()).contains("2026.03.01").contains("14:00");

        assertThat(response.getMainCards().getCard2().getOneLineSummary()).isEqualTo("요약1");
        assertThat(response.getMainCards().getCard2().getSpeakerName()).isEqualTo("Alice");

        assertThat(response.getMainCards().getCard3().getOneLineSummary()).isEqualTo("요약2");
        assertThat(response.getMainCards().getCard3().getSpeakerName()).isEqualTo("Bob");
    }
}