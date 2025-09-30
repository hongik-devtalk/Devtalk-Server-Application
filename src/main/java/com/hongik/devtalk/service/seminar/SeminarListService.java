package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.seminar.dto.SeminarListDto;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeminarListService {

    private final SeminarRepository seminarRepository;

    public SeminarListDto.SeminarResDtoList seminarList(){

        List<SeminarListDto.SeminarResDto> seminarDTOlist =
                seminarRepository.findAllByOrderBySeminarNumDesc()
                        .stream()
                        .map(s -> SeminarListDto.SeminarResDto.builder()
                                .seminarNum(s.getSeminarNum())
                                .seminarTopic(s.getTopic())
                                .seminarDate(s.getSeminarDate())
                                .place(s.getPlace())
                                .build())
                        .toList();

        return SeminarListDto.SeminarResDtoList.builder()
                .seminarList(seminarDTOlist).build();
    }
}
