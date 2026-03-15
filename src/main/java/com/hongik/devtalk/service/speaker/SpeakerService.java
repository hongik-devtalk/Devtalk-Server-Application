package com.hongik.devtalk.service.speaker;


import com.hongik.devtalk.domain.Speaker;
import com.hongik.devtalk.domain.speaker.dto.SpeakerDetailResponseDto;
import com.hongik.devtalk.domain.speaker.dto.SpeakerSearchResponseDto;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.speaker.SpeakerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)//데이터 읽기만 함
public class SpeakerService {

    private final SpeakerRepository speakerRepository;


    //연사 검색
    public List<SpeakerSearchResponseDto> searchSpeakers(String keyword) {
        //키워드 받아와서 세미나 검색

        List<Speaker> speakers;

        //만약에 키워드가 비어있으면 전체 조회
        if (keyword == null || keyword.isBlank()) {
            speakers = speakerRepository.findAll();
        } else {
            //공백 앞뒤까지 체크
            speakers = speakerRepository.findByNameContaining(keyword.trim());
        }

        //엔티티 리스트 -> dto 리스트로 변환 !
        return speakers.stream()
                .map(SpeakerSearchResponseDto::from)
                .collect(Collectors.toList());

    }

    //모든 연사 목록 조회

    public List<SpeakerSearchResponseDto> getAllSpeakers() {

        List<Speaker> speakers=speakerRepository.findAll();

        if(speakers.isEmpty())
        {
            throw new GeneralException(GeneralErrorCode.SPEAKER_NOT_FOUND);
        }


        //엔티티 리스트 -> dto 리스트로 변환 !
        return speakers.stream()
                .map(SpeakerSearchResponseDto::from)
                .collect(Collectors.toList());

    }

    //연사 상세정보 조회

    public SpeakerDetailResponseDto getSpeakerDetails(Long speakerId) {

        // 연사 조회
        Speaker speaker = speakerRepository.findById(speakerId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SPEAKER_NOT_FOUND));

        return SpeakerDetailResponseDto.from(speaker);
    }
}
