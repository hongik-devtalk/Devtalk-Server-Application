package com.hongik.devtalk.domain;

import com.hongik.devtalk.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "mainpage_images")
public class MainpageImages extends BaseTimeEntity {

    @Id
    @Column(length = 255, nullable = false)
    private String id;

    // Intro Image Fields
    @Column(name = "intro_image_id", length = 255)
    private String introImageId;
    
    @Column(name = "intro_url", length = 500)
    private String introUrl;
    
    @Column(name = "intro_file_name", length = 255)
    private String introFileName;
    
    @Column(name = "intro_content_type", length = 100)
    private String introContentType;
    
    @Column(name = "intro_updated_by", length = 255)
    private String introUpdatedBy;

    // Seminar Image Fields
    @Column(name = "seminar_image_id", length = 255)
    private String seminarImageId;
    
    @Column(name = "seminar_url", length = 500)
    private String seminarUrl;
    
    @Column(name = "seminar_file_name", length = 255)
    private String seminarFileName;
    
    @Column(name = "seminar_content_type", length = 100)
    private String seminarContentType;
    
    @Column(name = "seminar_updated_by", length = 255)
    private String seminarUpdatedBy;

    // 업데이트 메서드들
    public void updateIntroImage(String imageId, String url, String fileName, 
                               String contentType, String updatedBy) {
        this.introImageId = imageId;
        this.introUrl = url;
        this.introFileName = fileName;
        this.introContentType = contentType;
        this.introUpdatedBy = updatedBy;
    }

    public void updateSeminarImage(String imageId, String url, String fileName,
                                 String contentType, String updatedBy) {
        this.seminarImageId = imageId;
        this.seminarUrl = url;
        this.seminarFileName = fileName;
        this.seminarContentType = contentType;
        this.seminarUpdatedBy = updatedBy;
    }

    public void removeIntroImage() {
        this.introImageId = null;
        this.introUrl = null;
        this.introFileName = null;
        this.introContentType = null;
        this.introUpdatedBy = null;
    }

    public void removeSeminarImage() {
        this.seminarImageId = null;
        this.seminarUrl = null;
        this.seminarFileName = null;
        this.seminarContentType = null;
        this.seminarUpdatedBy = null;
    }

    // Helper methods to check if images exist
    public boolean hasIntroImage() {
        return introImageId != null && introUrl != null;
    }

    public boolean hasSeminarImage() {
        return seminarImageId != null && seminarUrl != null;
    }
}
