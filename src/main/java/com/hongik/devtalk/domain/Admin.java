package com.hongik.devtalk.domain;

import com.hongik.devtalk.domain.enums.AdminStatus;
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
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String loginPw;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminStatus status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
