package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.Applicant;
import com.hongik.devtalk.domain.Attendance;
import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.Student;
import com.hongik.devtalk.domain.enums.AttendanceStatus;
import com.hongik.devtalk.domain.enums.Department;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarStatisticsResponseDTO;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.ApplicantRepository;
import com.hongik.devtalk.repository.AttendanceRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeminarStatisticsService {

    private final SeminarRepository seminarRepository;
    private final ApplicantRepository applicantRepository;
    private final AttendanceRepository attendanceRepository;

    /**
     * 세미나 ID로 특정 세미나의 통계 정보를 조회합니다.
     *
     * @param seminarId 세미나 ID
     * @return 세미나 통계 정보 DTO
     * @throws GeneralException 세미나가 존재하지 않을 경우
     */
    public SeminarStatisticsResponseDTO getSeminarStatistics(Long seminarId) {
        // 세미나 존재 여부 확인
        Seminar seminar = seminarRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

        // 세미나 신청자 목록 조회
        List<Applicant> applicants = applicantRepository.findApplicantsBySeminarId(seminarId);
        int totalApplicants = applicants.size();

        // 학과별 집계
        Map<String, Long> departmentCountMap = new HashMap<>();
        for (Applicant applicant : applicants) {
            Student student = applicant.getStudent();
            
            // 복수 전공인 경우 각 학과에 모두 카운트
            Set<Department> departments = student.getDepartments();
            if (departments != null && !departments.isEmpty()) {
                for (Department department : departments) {
                    String deptName = department.name();
                    departmentCountMap.put(deptName, departmentCountMap.getOrDefault(deptName, 0L) + 1);
                }
            }
            
            // departmentEtc가 있는 경우 "기타"로 카운트
            if (student.getDepartmentEtc() != null && !student.getDepartmentEtc().isBlank()) {
                departmentCountMap.put("기타", departmentCountMap.getOrDefault("기타", 0L) + 1);
            }
        }

        // 학과별 DTO 생성 및 퍼센트 계산
        List<SeminarStatisticsResponseDTO.DepartmentRatioDTO> departmentRatios = departmentCountMap.entrySet().stream()
                .map(entry -> {
                    double percentage = totalApplicants > 0 
                            ? (entry.getValue().doubleValue() / totalApplicants) * 100.0 
                            : 0.0;
                    return SeminarStatisticsResponseDTO.DepartmentRatioDTO.builder()
                            .department(entry.getKey())
                            .count(entry.getValue())
                            .percentage(Math.round(percentage * 100.0) / 100.0) // 소수점 둘째 자리까지
                            .build();
                })
                .sorted(Comparator.comparing(SeminarStatisticsResponseDTO.DepartmentRatioDTO::getCount).reversed())
                .collect(Collectors.toList());

        // 학년별 집계
        Map<String, Long> gradeCountMap = new HashMap<>();
        for (Applicant applicant : applicants) {
            Student student = applicant.getStudent();
            String gradeKey;
            
            if (student.getGrade() != null) {
                gradeKey = student.getGrade() + "학년";
            } else if (student.getGradeEtc() != null && !student.getGradeEtc().isBlank()) {
                gradeKey = student.getGradeEtc();
            } else {
                gradeKey = "미입력";
            }
            
            gradeCountMap.put(gradeKey, gradeCountMap.getOrDefault(gradeKey, 0L) + 1);
        }

        // 학년별 DTO 생성 및 퍼센트 계산
        List<SeminarStatisticsResponseDTO.GradeRatioDTO> gradeRatios = gradeCountMap.entrySet().stream()
                .map(entry -> {
                    double percentage = totalApplicants > 0 
                            ? (entry.getValue().doubleValue() / totalApplicants) * 100.0 
                            : 0.0;
                    return SeminarStatisticsResponseDTO.GradeRatioDTO.builder()
                            .grade(entry.getKey())
                            .count(entry.getValue())
                            .percentage(Math.round(percentage * 100.0) / 100.0) // 소수점 둘째 자리까지
                            .build();
                })
                .sorted((a, b) -> {
                    // 학년 순서로 정렬 (1학년, 2학년, 3학년, 4학년, 기타, 미입력)
                    String gradeA = a.getGrade();
                    String gradeB = b.getGrade();
                    
                    if (gradeA.contains("학년") && gradeB.contains("학년")) {
                        int numA = Integer.parseInt(gradeA.replaceAll("[^0-9]", ""));
                        int numB = Integer.parseInt(gradeB.replaceAll("[^0-9]", ""));
                        return Integer.compare(numA, numB);
                    } else if (gradeA.contains("학년")) {
                        return -1;
                    } else if (gradeB.contains("학년")) {
                        return 1;
                    } else {
                        return gradeA.compareTo(gradeB);
                    }
                })
                .collect(Collectors.toList());

        // 출석 정보 집계
        long presentCount = 0;
        for (Applicant applicant : applicants) {
            // 해당 세미나의 출석 정보 찾기
            Optional<Attendance> attendanceOpt = applicant.getAttendances().stream()
                    .filter(att -> att.getSeminar().getId().equals(seminarId))
                    .findFirst();
            
            if (attendanceOpt.isPresent() && attendanceOpt.get().getStatus() == AttendanceStatus.PRESENT) {
                presentCount++;
            }
        }

        // 참석률 계산
        double attendanceRate = totalApplicants > 0 
                ? (presentCount * 100.0 / totalApplicants) 
                : 0.0;

        // 출석 요약 DTO 생성
        SeminarStatisticsResponseDTO.AttendanceSummaryDTO attendanceSummary = 
                SeminarStatisticsResponseDTO.AttendanceSummaryDTO.builder()
                        .totalApplicants((long) totalApplicants)
                        .presentCount(presentCount)
                        .attendanceRate(Math.round(attendanceRate * 100.0) / 100.0) // 소수점 둘째 자리까지
                        .build();

        return SeminarStatisticsResponseDTO.builder()
                .seminarNum(seminar.getSeminarNum())
                .departmentRatios(departmentRatios)
                .gradeRatios(gradeRatios)
                .attendanceSummary(attendanceSummary)
                .build();
    }
}
