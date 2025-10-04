package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 👈 1. 자동 증가하는 새 기본 키(ID) 추가
    private Long id;

    @Column(unique = true) // 👈 2. adminId는 이제 고유한 값을 갖는 일반 컬럼 (null 허용)
    private Long adminId;

    @Column(unique = true) // 👈 3. studentId도 고유한 값을 갖는 일반 컬럼 (null 허용)
    private Long studentId;

    @Column(nullable = false)
    private String token;

    // 학생용 생성자
    public RefreshToken(Long studentId, String token) {
        this.studentId = studentId;
        this.token = token;
    }

    // 관리자용 생성자
    public static RefreshToken createAdminToken(Long adminId, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.adminId = adminId;
        refreshToken.token = token;
        return refreshToken;
    }
}
