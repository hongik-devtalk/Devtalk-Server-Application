package com.hongik.devtalk.service.security;

import com.hongik.devtalk.domain.Admin;
import com.hongik.devtalk.global.security.CustomUserDetails;
import com.hongik.devtalk.repository.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        Admin admin = adminRepository.findByLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException(loginId));

        return new CustomUserDetails(admin);
    }
}
