package com.hongik.devtalk.domain.seminar.admin.dto;

import com.hongik.devtalk.domain.Applicant;
import com.hongik.devtalk.domain.Attendance;
import com.hongik.devtalk.domain.Student;
import com.hongik.devtalk.domain.enums.AttendanceStatus;
import com.hongik.devtalk.domain.enums.Department;
import com.hongik.devtalk.domain.enums.ParticipationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantResponseDTO {

    private int seminarNum;
    private List<StudentInfoDTO> students;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentInfoDTO {
        private String topic;
        private String studentId;
        private String studentNum;
        private String department;
        private String grade;
        private String name;
        private String phone;
        private String email;
        private ParticipationType participationType;
        private String inflowPath;
        private Boolean attendanceCheck;
        private LocalDateTime appliedAt;
        private LocalDateTime checkInTime;

        public static StudentInfoDTO from(Applicant applicant, String topic) {
            Student student = applicant.getStudent();

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

            // inflowPathEtc가 없으면 inflowPath를 사용
            String inflowPathInfo = (applicant.getInflowPathEtc() != null)
                    ? applicant.getInflowPathEtc()
                    : applicant.getInflowPath().name();

            // 출석 여부 확인 및 조회
            Optional<Attendance> attendance = applicant.getAttendances().stream()
                    .filter(att -> att.getSeminar().equals(applicant.getSeminar()))
                    .findFirst();

            Boolean attendanceCheck = attendance
                    .map(att -> att.getStatus() == AttendanceStatus.PRESENT)
                    .orElse(false);

            LocalDateTime checkInTime = attendance
                    .map(Attendance::getCheckInTime)
                    .orElse(null);

            return StudentInfoDTO.builder()
                    .topic(topic)
                    .studentId(student.getId().toString())
                    .studentNum(student.getStudentNum())
                    .department(departments)
                    .grade(gradeInfo)
                    .name(student.getName())
                    .phone(student.getPhone())
                    .email(student.getEmail())
                    .participationType(applicant.getParticipationType())
                    .inflowPath(inflowPathInfo)
                    .attendanceCheck(attendanceCheck)
                    .appliedAt(applicant.getCreatedAt())
                    .checkInTime(checkInTime)
                    .build();
        }
    }
}
