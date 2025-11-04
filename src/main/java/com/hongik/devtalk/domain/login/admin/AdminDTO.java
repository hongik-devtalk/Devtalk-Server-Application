package com.hongik.devtalk.domain.login.admin;

import com.hongik.devtalk.domain.Admin;
import lombok.*;

import java.util.List;

public class AdminDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginIdResDTO {
        Long adminId;
        String name;
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
        List<LoginIdResDTO> dtoList = adminList.stream()
                .map(admin -> {
                    return LoginIdResDTO.builder()
                            .adminId(admin.getId())
                            .name(admin.getName())
                            .loginId(admin.getLoginId())
                            .build();
                })
                .toList();

        return LoginIdResDTOList.builder()
                .adminIdList(dtoList)
                .build();
    }

    /*
    private static String maskLoginId(String loginId) {
        if (loginId == null || loginId.length() <= 2) {
            return loginId;
        }
        String prefix = loginId.substring(0, 2);
        String masked = "*".repeat(loginId.length() - 2);
        return prefix + masked;
    }
     */

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinAdminResDTO {
        Long adminId;
    }

    public static JoinAdminResDTO toJoinAdminResDTO (Admin admin) {
        return JoinAdminResDTO.builder().adminId(admin.getId()).build();
    }

}
