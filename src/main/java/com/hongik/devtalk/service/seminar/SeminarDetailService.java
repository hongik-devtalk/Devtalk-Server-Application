package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.Review;
import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.Session;
import com.hongik.devtalk.domain.seminar.detail.dto.SeminarDetailResponseDto;
import com.hongik.devtalk.domain.seminar.detail.dto.SeminarDetailReviewResponseDto;
import com.hongik.devtalk.domain.seminar.detail.dto.SeminarDetailSessionResponseDto;
import com.hongik.devtalk.domain.seminar.detail.dto.SeminarSearchResponseDto;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.seminar.SeminarDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)//데이터 읽기만 함
public class SeminarDetailService {

    private final SeminarDetailRepository seminarDetailRepository;

    //세미나 세부정보 조회 ( 세션 )

    public List<SeminarDetailSessionResponseDto> getSeminarDetailSession(Long seminarId) {

        //세미나 id 받아와서 seminar 존재하는지 확인
        Seminar seminar = seminarDetailRepository.findById(seminarId)
                .orElseThrow(()->new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));
        //존재하면 해당 세미나에 속한 모든 세션 목록을 데이터베이스에서 가져옴
        List<Session> sessions =seminar.getSessions();

        return sessions.stream()
                .map(SeminarDetailSessionResponseDto::from)
                .collect(Collectors.toList());


    }

    //세미나 세부정보 조회 ( 리뷰 )
    public List<SeminarDetailReviewResponseDto> getSeminarDetailReview(Long seminarId) {
        //세미나 id 받아와서 seminar 존재하는지 확인
        Seminar seminar = seminarDetailRepository.findById(seminarId)
                .orElseThrow(()->new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

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
            .orElseThrow(()->new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

    return SeminarDetailResponseDto.from(seminar);

}

    //세미나 검색
    public List<SeminarSearchResponseDto> searchByKeyword(String keyword) {
        //키워드 받아와서 세미나 검색

        List<Seminar> seminars;

        //만약에 키워드가 비어있으면 전체 조회
        if(keyword ==null || keyword.isEmpty())
        { seminars = seminarDetailRepository.findAll();
    }
        else{
            //키워드포함 세미나 검색
            seminars = seminarDetailRepository.findByTopicContaining(keyword);
        }

        //엔티티 리스트 -> dto 리스트로 변환 !
        return seminars.stream()
                .map(SeminarSearchResponseDto::from)
                .collect(Collectors.toList());

        }



}
