package com.hongik.devtalk.service;


import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.ShowSeminar;
import com.hongik.devtalk.domain.showseminar.dto.ShowSeminarRequestDTO;
import com.hongik.devtalk.domain.showseminar.dto.ShowSeminarResponseDTO;
import com.hongik.devtalk.repository.ShowSeminarRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShowSeminarService {
    private final ShowSeminarRepository showSeminarRepository;
    private final SeminarRepository seminarRepository;

    @Transactional
    public ShowSeminarResponseDTO updateShowSeminar(ShowSeminarRequestDTO request){
        // 1. 세미나 찾기
        Seminar seminar = null;
        if (request.getSeminarNum() != 0) {
            seminar = seminarRepository.findBySeminarNum(request.getSeminarNum())
                    .orElseThrow(() -> new IllegalArgumentException("해당 세미나 회차가 존재하지 않습니다."));
        }
        ShowSeminar showSeminar = showSeminarRepository.findAll().stream().findFirst()
                .orElse(ShowSeminar.builder().build()); // 없으면 새로 생성

        showSeminar.update(seminar, request.isApplicantActivate(), request.isLiveActivate());

        showSeminarRepository.save(showSeminar);

        return ShowSeminarResponseDTO.builder()
                .seminarId(seminar != null ? seminar.getId() : null)
                .seminarNum(seminar != null ? seminar.getSeminarNum() : null)
                .applicantActivate(request.isApplicantActivate())
                .liveActivate(request.isLiveActivate())
                .build();
    }


    @Transactional(readOnly = true)
    public ShowSeminarResponseDTO getCurrentShowSeminar() {
        ShowSeminar showSeminar = showSeminarRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("노출 세미나 정보가 없습니다."));

        Integer seminarNum = Optional.ofNullable(showSeminar.getSeminar())
                .map(Seminar::getSeminarNum)
                .orElse(null);

        Long seminarId = Optional.ofNullable(showSeminar.getSeminar())
                .map(Seminar::getId)
                .orElse(null);

        return ShowSeminarResponseDTO.builder()
                .seminarNum(seminarNum)
                .seminarId(seminarId)
                .applicantActivate(showSeminar.isApplicantActivate())
                .liveActivate(showSeminar.isLiveActivate())
                .build();
    }
}
