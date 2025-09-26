package com.hongik.devtalk.service.admin;

import com.hongik.devtalk.domain.Admin;
import com.hongik.devtalk.domain.login.AdminLoginDTO;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.global.security.JwtTokenProvider;
import com.hongik.devtalk.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.hongik.devtalk.domain.login.AdminLoginDTO.toLoginResDTO;
import static com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode.INVALID_LOGIN;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminCommandService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AdminLoginDTO.LoginResDTO loginAdmin (AdminLoginDTO.LoginReqDTO request) {

        Admin admin = adminRepository.findByLoginId(request.getLoginId()).orElseThrow(() -> new GeneralException(INVALID_LOGIN));

        if (!passwordEncoder.matches(request.getPassword(), admin.getLoginPw())) {
            throw new GeneralException(INVALID_LOGIN);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(admin.getLoginId(), null);
        String accessToken = jwtTokenProvider.generateToken(authentication);

        return toLoginResDTO(admin.getId(), accessToken, null);
    }

}
