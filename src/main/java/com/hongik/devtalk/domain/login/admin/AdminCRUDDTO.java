package com.hongik.devtalk.domain.login.admin;

import com.hongik.devtalk.domain.Admin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

public class AdminCRUDDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginIdResDTO {
        Long adminId;
        String loginId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginIdResDTOList {
        List<LoginIdResDTO> adminIdList;
    }

    public static LoginIdResDTOList toLoginIdResDTOList (List<Admin> adminList) {
        return null;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinAdminResDTO {
        Long adminId;
    }

    public static JoinAdminResDTO toJoinAdminResDTO (Admin admin) {
        return null;
    }

}
