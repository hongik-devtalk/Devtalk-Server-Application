package com.hongik.devtalk.domain.seminar.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarStatisticsResponseDTO {

    private int seminarNum;
    private List<DepartmentRatioDTO> departmentRatios;
    private List<GradeRatioDTO> gradeRatios;
    private AttendanceSummaryDTO attendanceSummary;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DepartmentRatioDTO {
        private String department;
        private Long count;
        private Double percentage;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GradeRatioDTO {
        private String grade;
        private Long count;
        private Double percentage;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttendanceSummaryDTO {
        private Long totalApplicants;
        private Long presentCount;
        private Double attendanceRate;
    }
}
