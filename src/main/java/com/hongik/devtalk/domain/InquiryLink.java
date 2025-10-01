package com.hongik.devtalk.domain;

import com.hongik.devtalk.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "inquiry_link")
public class InquiryLink extends BaseTimeEntity {

    @Id
    @Column(length = 255, nullable = false)
    private String id;

    @Column(name = "url", length = 2048, nullable = false)
    private String url;

    @Column(name = "updated_by", length = 255)
    private String updatedBy;


    public void updateLink(String url, String updatedBy) {
        this.url = url;
        this.updatedBy = updatedBy;
    }
}
