package com.hongik.devtalk.service.seminar;


import com.hongik.devtalk.domain.LiveFile;
import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarVideoRequestDTO;
import com.hongik.devtalk.domain.seminar.dto.SeminarFileResponseDto;
import com.hongik.devtalk.domain.seminar.dto.SeminarVideoResponseDTO;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SeminarVideoLoadService {

    private final SeminarRepository seminarRepository;

/**
 * 세미나 녹화본 영상 url 등록
 *
 * @param seminarId 세미나 ID

 * @param seminarVideoRequest   영상 URL 요청 DTO
 * @throws GeneralException 세미나가 존재하지 않거나 URL 형식이 올바르지 않은 경우
 */


@Transactional
    public void createSeminarVideo(Long seminarId, SeminarVideoRequestDTO seminarVideoRequest) {
    //세미나 존재여부 확인
    Seminar seminar = seminarRepository.findById(seminarId)
            .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

    //url 형식 검증

    if (!isValidUrl(seminarVideoRequest.getSeminarVideoUrl())) {
        throw new GeneralException(GeneralErrorCode.INVALID_VIDEO_URL);
    }
    //영상 url 저장
    seminar.updateVideoUrl(seminarVideoRequest.getSeminarVideoUrl());

}
    private boolean isValidUrl(String url)
    {
        try
        {
            new URL(url).toURI();
            return url.startsWith("http://") || url.startsWith("https://");
        }catch(Exception e)
        {
            return false;
        }
    }

    /**
     * 세미나 녹화본 영상 url 조회
     *
     * @param seminarId 세미나 ID

     * @throws GeneralException 세미나가 존재하지 않거나 URL 형식이 올바르지 않은 경우
     */

    @Transactional

    public SeminarVideoResponseDTO getSeminarVideo(Long seminarId) {
        //세미나 조회
        Seminar seminar = seminarRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

        return SeminarVideoResponseDTO.from(seminar);
    }

    @Transactional(readOnly = true)
    public SeminarFileResponseDto getSeminarFile(Long seminarId) {
        // 1. 세미나 조회
        Seminar seminar = seminarRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

        // 2. LiveFile 리스트를 FileInfo DTO 리스트로 변환
        List<SeminarFileResponseDto.FileInfo> fileInfoList = seminar.getLiveFiles().stream()
                .map(SeminarFileResponseDto.FileInfo::from)
                .toList();

        // 3. 최종 DTO 구성
        return SeminarFileResponseDto.builder()
                .seminarId(seminar.getId())
                .fileInfos(fileInfoList)
                .build();
    }
}

