package com.hongik.devtalk.domain.login.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class AdminLoginDTO {

    @Getter
    @Setter
    public static class LoginReqDTO{
        @NotBlank(message = "아이디는 필수입니다.")
        String loginId;

        @NotBlank(message = "패스워드는 필수입니다.")
        String password;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResDTO{
        Long adminId;
        String accessToken;
        String refreshToken;
    }

    public static LoginResDTO toLoginResDTO(Long adminId, String accessToken, String refreshToken) {

        return LoginResDTO.builder()
                .adminId(adminId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
