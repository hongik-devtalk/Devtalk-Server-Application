package com.hongik.devtalk.service.userhome;

import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.userhome.dto.UserHomeSeminarInfoResponseDTO;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.userhome.SeminarInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SeminarInfoService {

    private final SeminarInfoRepository seminarInfoRepository;

    public UserHomeSeminarInfoResponseDTO getSeminarById(Long seminarId){
        Seminar seminar = seminarInfoRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));
        return UserHomeSeminarInfoResponseDTO.builder()
                .seminarId(seminar.getId())
                .seminarNum(seminar.getSeminarNum())
                .topic(seminar.getTopic())
                .seminarDate(seminar.getSeminarDate())
                .place(seminar.getPlace())
                .startDate(seminar.getStartDate())
                .endDate(seminar.getEndDate())
                .build();

    }
}
