package com.hongik.devtalk.service.admin;

import com.hongik.devtalk.domain.Admin;
import com.hongik.devtalk.domain.RefreshToken;
import com.hongik.devtalk.domain.login.admin.AdminLoginDTO;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.global.security.JwtTokenProvider;
import com.hongik.devtalk.repository.admin.AdminRepository;
import com.hongik.devtalk.repository.auth.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hongik.devtalk.domain.login.admin.AdminLoginDTO.toLoginResDTO;
import static com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommandService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public AdminLoginDTO.LoginResDTO loginAdmin (AdminLoginDTO.LoginReqDTO request) {

        Admin admin = adminRepository.findByLoginId(request.getLoginId()).orElseThrow(() -> new GeneralException(INVALID_LOGIN));

        if (!passwordEncoder.matches(request.getPassword(), admin.getLoginPw())) {
            throw new GeneralException(INVALID_LOGIN);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(admin.getLoginId(), null);
        String accessToken = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        if (refreshTokenRepository.existsByAdminId(admin.getId())) {
            RefreshToken token = refreshTokenRepository.findByAdminId(admin.getId());
            token.setToken(refreshToken);
        }
        else {
            refreshTokenRepository.save(new RefreshToken(admin.getId(), refreshToken));
        }

        return toLoginResDTO(admin.getId(), accessToken, refreshToken);
    }

    public AdminLoginDTO.LoginResDTO refreshAdmin (String refreshToken) {

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new GeneralException(INVALID_TOKEN);
        }

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> new GeneralException(INVALID_TOKEN));
        Admin admin = adminRepository.findById(token.getAdminId()).orElseThrow(() -> new GeneralException(ADMIN_NOT_FOUND));
        String loginId = admin.getLoginId();
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginId, null);
        String accessToken = jwtTokenProvider.generateToken(authentication);

        return toLoginResDTO(admin.getId(), accessToken, refreshToken);
    }

}
