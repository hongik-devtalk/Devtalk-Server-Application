package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class LiveFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    // 세미나별로 자료 제공
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id", nullable = false)
    private Seminar seminar;

    @Column(nullable = false)
    private String fileName;

    @Column(length = 2048, nullable = false)
    private String fileUrl;

    @Column(length = 50)
    private String fileType;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
