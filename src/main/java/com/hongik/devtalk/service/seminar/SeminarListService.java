package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.seminar.dto.SeminarListDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SeminarListService {

    public SeminarListDto.SeminarResDtoList seminarList(){
        return null;
    }
}
