package com.hongik.devtalk.domain.seminar.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongik.devtalk.domain.Review;
import com.hongik.devtalk.domain.Student;
import com.hongik.devtalk.domain.enums.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarReviewResponseDTO {
    private Long reviewId;
    private Integer score;
    private String department;
    private String grade;
    private String name;
    private String content;
    private String nextTopic;
    private Boolean isPublic;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static SeminarReviewResponseDTO from(Review review) {
        Student student = review.getStudent();

        // 학과 처리
        String departments = student.getDepartments().stream()
                .map(Department::name)
                .collect(Collectors.joining(", "));

        if (student.getDepartmentEtc() != null && !student.getDepartmentEtc().isBlank()) {
            departments = departments.isBlank()
                    ? student.getDepartmentEtc()
                    : departments + ", " + student.getDepartmentEtc();
        }

        String gradeInfo = (student.getGrade() != null)
                ? student.getGrade() + "학년"
                : student.getGradeEtc();

        return SeminarReviewResponseDTO.builder()
                .reviewId(review.getId())
                .score((int) review.getScore())
                .department(departments)
                .grade(gradeInfo)
                .name(student.getName())
                .content(review.getStrength())
                .nextTopic(review.getNextTopic())
                .isPublic(review.isPublic())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
