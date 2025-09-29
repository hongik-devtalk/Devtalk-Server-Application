package com.hongik.devtalk.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((auth) -> auth.disable())

                .formLogin((auth) -> auth.disable())

                .httpBasic((auth) -> auth.disable())

                //세미나 신청 API URL 나오면 수정
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin/login").permitAll()
                        // TODO: 추후 삭제
                        .requestMatchers("/admin/home/**").permitAll() // 임시: 홈페이지 관리 API 허용 (인증 구현 전) 추후 삭제
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger UI 허용
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll())
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
