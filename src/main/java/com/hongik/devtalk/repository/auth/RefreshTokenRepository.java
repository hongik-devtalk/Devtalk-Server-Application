package com.hongik.devtalk.repository.auth;

import com.hongik.devtalk.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    boolean existsByAdminId(Long adminId);

    RefreshToken findByAdminId(Long adminId);

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

    Optional<RefreshToken> findByStudentId(Long studentId);
}
