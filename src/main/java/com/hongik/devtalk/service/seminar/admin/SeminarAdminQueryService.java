package com.hongik.devtalk.service.seminar.admin;

import com.hongik.devtalk.domain.Applicant;
import com.hongik.devtalk.domain.seminar.admin.dto.ApplicantResponseDTO;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.seminar.admin.ApplicantRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SeminarAdminQueryService {

    private final ApplicantRepository applicantRepository;
    private final SeminarRepository seminarRepository;

    /**
     * 세미나 ID로 신청자 목록을 조회
     *
     * @param seminarId 세미나 ID
     * @return 신청자 정보 DTO 리스트
     * @throws GeneralException 세미나가 존재하지 않을 경우
     */
    public List<ApplicantResponseDTO> getApplicants(Long seminarId) {
        // 세미나 존재 여부 확인
        seminarRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

        // 세미나 신청자 목록 조회
        List<Applicant> applicants = applicantRepository.findApplicantsBySeminarId(seminarId);

        return applicants.stream()
                .map(applicant -> ApplicantResponseDTO.builder()
                        .topic(applicant.getSeminar().getTopic())
                        .studentId(applicant.getStudent().getId().toString())
                        .studentNum(applicant.getStudent().getStudentNum())
                        .department(applicant.getStudent().getStudentDepartments().stream()
                                .map(sd -> sd.getDepartment().getDepartmentName())
                                .collect(Collectors.joining(","))) // 여러 학과일 경우 ,로 join
                        .grade(applicant.getStudent().getGrade() != null
                                ? applicant.getStudent().getGrade().toString()
                                : applicant.getStudent().getGradeEtc()) // grade 없으면 gradeEtc
                        .name(applicant.getStudent().getName())
                        .phone(applicant.getStudent().getPhone())
                        .participationType(applicant.getParticipationType())
                        .inflowPath(applicant.getInflowPath())
                        .build())
                .collect(Collectors.toList());
    }

}
