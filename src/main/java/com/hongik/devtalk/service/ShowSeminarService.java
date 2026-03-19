package com.hongik.devtalk.service;

import com.hongik.devtalk.domain.MainpageImages;
import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.Session;
import com.hongik.devtalk.domain.ShowSeminar;
import com.hongik.devtalk.domain.showseminar.dto.ShowSeminarRequestDTO;
import com.hongik.devtalk.domain.showseminar.dto.ShowSeminarResponseDTO;
import com.hongik.devtalk.repository.SessionRepository;
import com.hongik.devtalk.repository.mainpage.MainpageImagesRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import com.hongik.devtalk.repository.seminar.ShowSeminarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShowSeminarService {
    private static final DateTimeFormatter CARD1_SCHEDULE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd (E) HH:mm");

    private final ShowSeminarRepository showSeminarRepository;
    private final SeminarRepository seminarRepository;
    private final SessionRepository sessionRepository;
    private final MainpageImagesRepository mainpageImagesRepository;

    @Transactional
    public ShowSeminarResponseDTO updateShowSeminar(ShowSeminarRequestDTO request) {
        Seminar seminar = null;
        if (request.getSeminarNum() != null) {
            seminar = seminarRepository.findBySeminarNum(request.getSeminarNum())
                    .orElseThrow(() -> new IllegalArgumentException("해당 세미나 회차가 존재하지 않습니다."));
        }

        ShowSeminar showSeminar = showSeminarRepository.findFirstByOrderByIdAsc()
                .orElse(ShowSeminar.builder().build());

        showSeminar.update(seminar, request.isApplicantActivate(), request.isLiveActivate());
        showSeminarRepository.save(showSeminar);

        return ShowSeminarResponseDTO.builder()
                .seminarId(seminar != null ? seminar.getId() : null)
                .seminarNum(seminar != null ? seminar.getSeminarNum() : null)
                .applicantActivate(request.isApplicantActivate())
                .liveActivate(request.isLiveActivate())
                .mainPosterImageUrl(getMainPosterImageUrl())
                .mainCards(buildMainCards(seminar))
                .build();
    }

    @Transactional(readOnly = true)
    public ShowSeminarResponseDTO getCurrentShowSeminar() {
        ShowSeminar showSeminar = showSeminarRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new IllegalStateException("노출 세미나 정보가 없습니다."));

        Seminar seminar = showSeminar.getSeminar();

        return ShowSeminarResponseDTO.builder()
                .seminarNum(Optional.ofNullable(seminar).map(Seminar::getSeminarNum).orElse(null))
                .seminarId(Optional.ofNullable(seminar).map(Seminar::getId).orElse(null))
                .applicantActivate(showSeminar.isApplicantActivate())
                .liveActivate(showSeminar.isLiveActivate())
                .mainPosterImageUrl(getMainPosterImageUrl())
                .mainCards(buildMainCards(seminar))
                .build();
    }

    private String getMainPosterImageUrl() {
        return mainpageImagesRepository.findTopByOrderById()
                .map(MainpageImages::getIntroUrl)
                .orElse(null);
    }

    private ShowSeminarResponseDTO.MainCards buildMainCards(Seminar seminar) {
        if (seminar == null) {
            return null;
        }

        List<Session> sessions = sessionRepository.findBySeminarIdWithSpeaker(seminar.getId());

        ShowSeminarResponseDTO.SeminarRoundCard card1 = ShowSeminarResponseDTO.SeminarRoundCard.builder()
                .imageUrl(seminar.getThumbnailUrl())
                .seminarTitle(seminar.getTopic())
                .schedule(seminar.getSeminarDate() != null ? seminar.getSeminarDate().format(CARD1_SCHEDULE_FORMATTER) : null)
                .place(seminar.getPlace())
                .build();

        ShowSeminarResponseDTO.SessionCard card2 = toSessionCard(seminar, sessions, 0);
        ShowSeminarResponseDTO.SessionCard card3 = toSessionCard(seminar, sessions, 1);

        return ShowSeminarResponseDTO.MainCards.builder()
                .card1(card1)
                .card2(card2)
                .card3(card3)
                .build();
    }

    private ShowSeminarResponseDTO.SessionCard toSessionCard(Seminar seminar, List<Session> sessions, int index) {
        if (sessions.size() <= index) {
            return null;
        }

        Session session = sessions.get(index);
        return ShowSeminarResponseDTO.SessionCard.builder()
                .imageUrl(session.getSpeaker() != null ? session.getSpeaker().getProfileUrl() : null)
                .seminarTitle(seminar.getTopic())
                .oneLineSummary(session.getOneLineSummary())
                .speakerName(session.getSpeaker() != null ? session.getSpeaker().getName() : null)
                .build();
    }
}