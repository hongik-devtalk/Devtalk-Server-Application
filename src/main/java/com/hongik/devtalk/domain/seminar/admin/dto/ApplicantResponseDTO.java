package com.hongik.devtalk.domain.seminar.admin.dto;

import com.hongik.devtalk.domain.Applicant;
import com.hongik.devtalk.domain.Student;
import com.hongik.devtalk.domain.enums.Department;
import com.hongik.devtalk.domain.enums.InflowPath;
import com.hongik.devtalk.domain.enums.ParticipationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantResponseDTO {
    private String topic;
    private String studentId;
    private String studentNum;
    private String department;
    private String grade;
    private String name;
    private String phone;
    private ParticipationType participationType;
    private InflowPath inflowPath;

    public static ApplicantResponseDTO from(Applicant applicant, String topic) {
        Student student = applicant.getStudent();

        // 복수 전공일 경우 학과명을 ,로 연결
        String departmentNames = student.getDepartments().stream()
                .map(Department::name)
                .collect(Collectors.joining(", "));

        // 숫자 학년이 있으면 숫자를, 없으면 gradeEtc를 사용
        String gradeInfo = (student.getGrade() != null)
                ? student.getGrade() + "학년"
                : student.getGradeEtc();

        return ApplicantResponseDTO.builder()
                .topic(topic)
                .studentId(student.getId().toString())
                .studentNum(student.getStudentNum())
                .department(departmentNames)
                .grade(gradeInfo)
                .name(student.getName())
                .phone(student.getPhone())
                .participationType(applicant.getParticipationType())
                .inflowPath(applicant.getInflowPath())
                .build();
    }
}