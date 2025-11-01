package com.hongik.devtalk.domain.login.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

public class AdminLoginDTO {

    @Getter
    @Setter
    public static class LoginReqDTO{
        @NotBlank(message = "아이디는 필수입니다.")
        @Pattern(
                regexp = "^[A-Za-z0-9!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]{8,16}$",
                message = "아이디는 영어/숫자/특수문자만 가능하며, 공백 없이 8~16자여야 합니다."
        )
        String loginId;

        @Pattern(
                regexp = "^[A-Za-z0-9!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]{8,16}$",
                message = "패스워드는 영어/숫자/특수문자만 가능하며, 공백 없이 8~16자여야 합니다."
        )
        @NotBlank(message = "패스워드는 필수입니다.")
        String password;
    }

    @Getter
    @Setter
    public static class JoinReqDTO{

        String name;

        @NotBlank(message = "아이디는 필수입니다.")
        @Pattern(
                regexp = "^[A-Za-z0-9!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]{8,16}$",
                message = "아이디는 영어/숫자/특수문자만 가능하며, 공백 없이 8~16자여야 합니다."
        )
        String loginId;

        @Pattern(
                regexp = "^[A-Za-z0-9!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]{8,16}$",
                message = "패스워드는 영어/숫자/특수문자만 가능하며, 공백 없이 8~16자여야 합니다."
        )
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
