package com.hongik.devtalk.domain.seminar.admin.dto;

import com.hongik.devtalk.domain.Applicant;
import com.hongik.devtalk.domain.Student;
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

        String departmentNames = student.getStudentDepartments().stream()
                .map(sd -> sd.getDepartment().getDepartmentName())
                .collect(Collectors.joining(","));

        String gradeInfo = (student.getGrade() != null)
                ? student.getGrade().toString()
                : student.getGradeEtc();

        return ApplicantResponseDTO.builder()
                .topic(topic)   // 외부에서 받은 값
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