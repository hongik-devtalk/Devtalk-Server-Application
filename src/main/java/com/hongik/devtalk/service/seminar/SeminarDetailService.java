package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.*;
import com.hongik.devtalk.domain.seminar.detail.dto.*;
import com.hongik.devtalk.domain.speaker.dto.SpeakerSearchResponseDto;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.SeminarTagRepository;
import com.hongik.devtalk.repository.SpeakerTagRepository;
import com.hongik.devtalk.repository.TagRepository;
import com.hongik.devtalk.repository.seminar.SeminarDetailRepository;
import com.hongik.devtalk.repository.speaker.SpeakerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)//데이터 읽기만 함
public class SeminarDetailService {

    private final SeminarDetailRepository seminarDetailRepository;
    private final SpeakerRepository speakerRepository;
    private final SeminarTagRepository seminarTagRepository;
    private final SpeakerTagRepository speakerTagRepository;
    private final TagRepository tagRepository;

    //세미나 세부정보 조회 ( 세션 )

    public List<SeminarDetailSessionResponseDto> getSeminarDetailSession(Long seminarId) {

        //세미나 id 받아와서 seminar 존재하는지 확인
        Seminar seminar = seminarDetailRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));
        //존재하면 해당 세미나에 속한 모든 세션 목록을 데이터베이스에서 가져옴
        List<Session> sessions = seminar.getSessions();

        return sessions.stream()
                .map(SeminarDetailSessionResponseDto::from)
                .collect(Collectors.toList());


    }

    //세미나 세부정보 조회 ( 리뷰 )
    public List<SeminarDetailReviewResponseDto> getSeminarDetailReview(Long seminarId) {
        //세미나 id 받아와서 seminar 존재하는지 확인
        Seminar seminar = seminarDetailRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

        //존재하면 해당 세미나에 속한 모든 리뷰 목록을 데이터베이스에서 가져옴
        List<Review> reviews = seminar.getReviews();

        return reviews.stream()
                .map(SeminarDetailReviewResponseDto::from)
                .collect(Collectors.toList());
    }

    //세미나 세부정보 조회 ( 세미나 )

    public SeminarDetailResponseDto getSeminarDetail(Long seminarId) {
        //세미나 id를 받아와서 seminar 존재하는지 확인
        Seminar seminar = seminarDetailRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

        List<Session> sortedSessions = seminar.getSessions().stream()
                .sorted(Comparator.comparing(Session::getId))
                .toList();
        List<SeminarDetailSessionResponseDto> sessions = sortedSessions.stream()
                .map(SeminarDetailSessionResponseDto::from)
                .toList();

        return SeminarDetailResponseDto.from(seminar, sessions);

    }

    //세미나 검색
    public List<SeminarSearchResponseDto> searchSeminars(String keyword) {
        //키워드 받아와서 세미나 검색

        List<Seminar> seminars;

        //만약에 키워드가 비어있으면 전체 조회
        if (keyword == null || keyword.isEmpty()) {
            seminars = seminarDetailRepository.findAll();
        } else {
            //키워드포함 세미나 검색
            seminars = seminarDetailRepository.findByTopicContaining(keyword);
        }

        //엔티티 리스트 -> dto 리스트로 변환 !
        return seminars.stream()
                .map(SeminarSearchResponseDto::from)
                .collect(Collectors.toList());

    }

    @Transactional
    public TagSearchResponseDto searchAllByTag(String tagText) {
        if (tagText == null || tagText.isBlank()) {
            return TagSearchResponseDto.builder()
                    .seminars(List.of())
                    .speakers(List.of())
                    .build();
        }

        String query = tagText.trim();

        Map<Long, Seminar> seminarMap = new LinkedHashMap<>();
        seminarTagRepository.findByTag_TagTextIgnoreCase(query).stream()
                .map(SeminarTag::getSeminar)
                .forEach(seminar -> seminarMap.put(seminar.getId(), seminar));

        Map<Long, Speaker> speakerMap = new LinkedHashMap<>();
        speakerTagRepository.findByTag_TagTextIgnoreCase(query).stream()
                .map(SpeakerTag::getSpeaker)
                .forEach(speaker -> {
                    speakerMap.put(speaker.getId(), speaker);
                    speaker.getSessions().forEach(session ->
                            seminarMap.put(session.getSeminar().getId(), session.getSeminar())
                    );
                });

        tagRepository.findByTagTextIgnoreCase(query)
                .ifPresent(Tag::increaseSearchCount);

        return TagSearchResponseDto.builder()
                .seminars(seminarMap.values().stream()
                        .map(SeminarSearchResponseDto::from)
                        .toList())
                .speakers(speakerMap.values().stream()
                        .map(SpeakerSearchResponseDto::from)
                        .toList())
                .build();
    }

    @Transactional
    public List<SeminarSearchResponseDto> searchSeminarsByTag(String tagText) {
        if (tagText == null || tagText.isBlank()) {
            return List.of();
        }

        String query = tagText.trim();

        List<Seminar> seminarTaggedSeminars = seminarTagRepository.findByTag_TagTextIgnoreCase(query).stream()
                .map(SeminarTag::getSeminar)
                .toList();

        List<Seminar> speakerTaggedSeminars = speakerTagRepository.findByTag_TagTextIgnoreCase(query).stream()
                .map(SpeakerTag::getSpeaker)
                .flatMap(speaker -> speaker.getSessions().stream())
                .map(Session::getSeminar)
                .toList();

        Map<Long, Seminar> seminarMap = new LinkedHashMap<>();

        seminarTaggedSeminars.forEach(seminar -> seminarMap.put(seminar.getId(), seminar));
        speakerTaggedSeminars.forEach(seminar -> seminarMap.put(seminar.getId(), seminar));

        tagRepository.findByTagTextIgnoreCase(query)
                .ifPresent(Tag::increaseSearchCount);

        return seminarMap.values().stream()
                .map(SeminarSearchResponseDto::from)
                .toList();
    }


    public List<String> getTop3PopularTags() {
        return tagRepository.findTop3ByOrderBySearchCountDesc().stream()
                .map(Tag::getTagText)
                .toList();
    }


}
