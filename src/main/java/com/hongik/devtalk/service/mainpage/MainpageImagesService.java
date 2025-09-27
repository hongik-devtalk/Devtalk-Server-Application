package com.hongik.devtalk.service.mainpage;

import com.hongik.devtalk.domain.MainpageImages;
import com.hongik.devtalk.domain.enums.ImageType;
import com.hongik.devtalk.domain.mainpage.dto.ImageInfoDto;
import com.hongik.devtalk.domain.mainpage.dto.MainpageImagesResponseDto;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.mainpage.MainpageImagesRepository;
import com.hongik.devtalk.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainpageImagesService {

    private final MainpageImagesRepository mainpageImagesRepository;
    private final S3Service s3Service;
    
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String MAINPAGE_IMAGES_ID = "mainpage_images_1"; // 고정 ID

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

    /**
     * 홍보 이미지를 업로드하거나 교체합니다.
     */
    @Transactional
    public ImageInfoDto uploadOrReplaceMainpageImage(ImageType type, MultipartFile file, String updatedBy) {
        // 파일 유효성 검사
        validateImageFile(file);
        
        // S3에 파일 업로드
        String s3Key = uploadImageToS3(file, type);
        
        // DB 업데이트
        MainpageImages mainpageImages = getOrCreateMainpageImages();
        
        String imageId = UUID.randomUUID().toString();
        
        // 기존 이미지가 있다면 S3에서 삭제
        deleteExistingImageFromS3(mainpageImages, type);
        
        // 새 이미지 정보로 업데이트
        if (type == ImageType.INTRO) {
            mainpageImages.updateIntroImage(imageId, s3Key, file.getOriginalFilename(), 
                                          file.getContentType(), updatedBy);
        } else {
            mainpageImages.updateSeminarImage(imageId, s3Key, file.getOriginalFilename(),
                                            file.getContentType(), updatedBy);
        }
        
        mainpageImagesRepository.save(mainpageImages);
        
        log.info("이미지 업로드 완료: type={}, imageId={}, url={}", type, imageId, s3Key);
        
        return ImageInfoDto.builder()
                .imageId(imageId)
                .url(s3Key)
                .fileName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .updatedAt(LocalDateTime.now())
                .updatedBy(updatedBy)
                .build();
    }
    
    /**
     * 이미지 파일의 유효성을 검증합니다.
     */
    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new GeneralException(GeneralErrorCode.FILE_EMPTY);
        }
        
        if (!s3Service.isValidImageFile(file)) {
            throw new GeneralException(GeneralErrorCode.UNSUPPORTED_FILE_TYPE);
        }
        
        if (!s3Service.isValidFileSize(file, MAX_FILE_SIZE)) {
            throw new GeneralException(GeneralErrorCode.FILE_TOO_LARGE);
        }
    }
    
    /**
     * S3에 이미지를 업로드합니다.
     */
    private String uploadImageToS3(MultipartFile file, ImageType type) {
        String folder = "home/images/" + type.name().toLowerCase();
        return s3Service.uploadFile(file, folder);
    }
    
    /**
     * MainpageImages 엔티티를 조회하거나 생성합니다.
     */
    private MainpageImages getOrCreateMainpageImages() {
        return mainpageImagesRepository.findTopByOrderById()
                .orElseGet(() -> MainpageImages.builder()
                        .id(MAINPAGE_IMAGES_ID)
                        .build());
    }
    
    /**
     * 기존 이미지를 S3에서 삭제합니다.
     */
    private void deleteExistingImageFromS3(MainpageImages mainpageImages, ImageType type) {
        String existingUrl = null;
        
        if (type == ImageType.INTRO && mainpageImages.hasIntroImage()) {
            existingUrl = mainpageImages.getIntroUrl();
        } else if (type == ImageType.PREVIOUS_SEMINAR && mainpageImages.hasSeminarImage()) {
            existingUrl = mainpageImages.getSeminarUrl();
        }
        
        if (existingUrl != null) {
            String s3Key = s3Service.extractS3KeyFromUrl(existingUrl);
            if (s3Key != null) {
                try {
                    s3Service.deleteFile(s3Key);
                    log.info("기존 이미지 삭제 완료: {}", s3Key);
                } catch (Exception e) {
                    log.warn("기존 이미지 삭제 실패: {}", s3Key, e);
                    // 삭제 실패해도 새 이미지 업로드는 계속 진행
                }
            }
        }
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
