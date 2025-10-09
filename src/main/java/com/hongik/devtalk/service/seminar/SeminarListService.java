package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.seminar.dto.SeminarListDto;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeminarListService {

    private final SeminarRepository seminarRepository;

    public SeminarListDto.SeminarResDtoList seminarList(){

        LocalDateTime now = LocalDateTime.now();

        List<SeminarListDto.SeminarResDto> seminarDTOlist =
                seminarRepository.findAllByOrderBySeminarNumDesc()
                        .stream()
                        .map(s -> {
                            boolean isActive = false;

                            if (s.getStartDate() != null && s.getEndDate() != null) {
                                isActive = now.isAfter(s.getStartDate()) && now.isBefore(s.getEndDate());
                            }

                            return SeminarListDto.SeminarResDto.builder()
                                    .seminarId(s.getId())
                                    .seminarNum(s.getSeminarNum())
                                    .seminarTopic(s.getTopic())
                                    .seminarDate(s.getSeminarDate())
                                    .place(s.getPlace())
                                    .imageUrl(s.getThumbnailUrl())
                                    .isActive(isActive) // ✅ 신청 가능 여부
                                    .build();
                        })
                        .toList();

        return SeminarListDto.SeminarResDtoList.builder()
                .seminarList(seminarDTOlist).build();
    }
}
