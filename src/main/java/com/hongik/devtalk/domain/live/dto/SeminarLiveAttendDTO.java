package com.hongik.devtalk.domain.live.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class SeminarLiveAttendDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttendStudentDTO {
        Long studentId;
        String studentNum;
        List<String> department;
        Integer grade;
        String studentName;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttendStudentDTOList {
        List<AttendStudentDTO> students;
    }

}
