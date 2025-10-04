package com.hongik.devtalk.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshToken {

    @Id
    private Long adminId;

    private Long studentId;

    private String token;

    public RefreshToken(Long studentId, String token) {
        this.studentId = studentId;
        this.token = token;
    }
}
