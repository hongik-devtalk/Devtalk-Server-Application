package com.hongik.devtalk.service.mainpage;

import com.hongik.devtalk.domain.MainpageImages;
import com.hongik.devtalk.domain.mainpage.dto.ImageInfoDto;
import com.hongik.devtalk.domain.mainpage.dto.MainpageImagesResponseDto;
import com.hongik.devtalk.repository.mainpage.MainpageImagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainpageImagesService {

    private final MainpageImagesRepository mainpageImagesRepository;

    public MainpageImagesResponseDto getMainpageImages() {
        Optional<MainpageImages> mainpageImagesOpt = mainpageImagesRepository.findTopByOrderById();
        
        if (mainpageImagesOpt.isEmpty()) {
            // 데이터가 없으면 둘 다 null인 응답 반환
            return MainpageImagesResponseDto.builder()
                    .intro(null)
                    .previousSeminar(null)
                    .build();
        }

        MainpageImages mainpageImages = mainpageImagesOpt.get();
        
        return MainpageImagesResponseDto.builder()
                .intro(convertToImageInfoDto(
                        mainpageImages.getIntroImageId(),
                        mainpageImages.getIntroUrl(),
                        mainpageImages.getIntroFileName(),
                        mainpageImages.getIntroContentType(),
                        mainpageImages.getUpdatedAt(),
                        mainpageImages.getIntroUpdatedBy()
                ))
                .previousSeminar(convertToImageInfoDto(
                        mainpageImages.getSeminarImageId(),
                        mainpageImages.getSeminarUrl(),
                        mainpageImages.getSeminarFileName(),
                        mainpageImages.getSeminarContentType(),
                        mainpageImages.getUpdatedAt(),
                        mainpageImages.getSeminarUpdatedBy()
                ))
                .build();
    }

    private ImageInfoDto convertToImageInfoDto(String imageId, String url, String fileName,
                                             String contentType, 
                                             java.time.LocalDateTime updatedAt, String updatedBy) {
        // 이미지 정보가 없으면 null 반환
        if (imageId == null || url == null) {
            return null;
        }

        return ImageInfoDto.builder()
                .imageId(imageId)
                .url(url)
                .fileName(fileName)
                .contentType(contentType)
                .updatedAt(updatedAt)
                .updatedBy(updatedBy)
                .build();
    }
}
