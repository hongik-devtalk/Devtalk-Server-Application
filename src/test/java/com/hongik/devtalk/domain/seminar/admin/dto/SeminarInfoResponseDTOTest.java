package com.hongik.devtalk.domain.seminar.admin.dto;

import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.Session;
import com.hongik.devtalk.domain.Speaker;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SeminarInfoResponseDTOTest {

    @Test
    void from_includesPartTagAndOneLineSummary() {
        Seminar seminar = Seminar.builder()
                .id(1L)
                .seminarNum(5)
                .topic("DevTalk 5")
                .seminarDate(LocalDateTime.of(2026, 1, 10, 18, 0))
                .startDate(LocalDateTime.of(2026, 1, 1, 9, 0))
                .endDate(LocalDateTime.of(2026, 1, 5, 18, 0))
                .place("K101")
                .thumbnailUrl("https://cdn/thumb.png")
                .thumbnailFileName("thumb")
                .thumbnailFileExtension("png")
                .thumbnailFileSize(100L)
                .build();

        Speaker speaker = Speaker.builder()
                .id(10L)
                .name("Alice")
                .organization("Org")
                .history("History")
                .profileUrl("https://cdn/profile.png")
                .profileFileName("profile")
                .profileFileExtension("png")
                .profileFileSize(200L)
                .build();

        Session session = Session.builder()
                .id(100L)
                .seminar(seminar)
                .speaker(speaker)
                .title("Session")
                .description("Description")
                .partTag("1ºÎ")
                .oneLineSummary("ÇÙ½É ¿ä¾à")
                .build();

        SeminarInfoResponseDTO response = SeminarInfoResponseDTO.from(seminar, null, List.of(), List.of(session));

        assertThat(response.getSpeakers()).hasSize(1);
        assertThat(response.getSpeakers().get(0).getPartTag()).isEqualTo("1ºÎ");
        assertThat(response.getSpeakers().get(0).getOneLineSummary()).isEqualTo("ÇÙ½É ¿ä¾à");
    }
}