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
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeminarReviewResponseDTO {

    private int seminarNum;
    private List<ReviewInfoDTO> reviews;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReviewInfoDTO {
        private Long reviewId;
        private String name;
        private String studentNum;
        private String department;
        private String grade;
        private Integer score;
        private String strength;
        private String improvement;
        private String nextTopic;
        private Boolean isPublic;
        private Boolean isFeatured;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd (E) HH:mm")
        private LocalDateTime createdAt;

        public static ReviewInfoDTO from(Review review) {
            Student student = review.getStudent();

            // 복수 전공일 경우 학과명을 ,로 연결
            String departments = student.getDepartments().stream()
                    .map(Department::name)
                    .collect(Collectors.joining(", "));

            if (student.getDepartmentEtc() != null && !student.getDepartmentEtc().isBlank()) {
                departments = departments.isBlank()
                        ? student.getDepartmentEtc()
                        : departments + ", " + student.getDepartmentEtc();
            }

            // 숫자 학년이 있으면 숫자를, 없으면 gradeEtc를 사용
            String gradeInfo = (student.getGrade() != null)
                    ? student.getGrade() + "학년"
                    : student.getGradeEtc();

            return ReviewInfoDTO.builder()
                    .reviewId(review.getId())
                    .name(student.getName())
                    .studentNum(student.getStudentNum())
                    .department(departments)
                    .grade(gradeInfo)
                    .score((int) review.getScore())
                    .strength(review.getStrength())
                    .improvement(review.getImprovement())
                    .nextTopic(review.getNextTopic())
                    .isPublic(review.isPublic())
                    .isFeatured(review.isNote())
                    .createdAt(review.getCreatedAt())
                    .build();
        }
    }
}