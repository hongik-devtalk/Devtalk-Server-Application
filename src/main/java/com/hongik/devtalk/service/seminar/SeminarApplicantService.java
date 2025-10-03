package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.*;
import com.hongik.devtalk.domain.enums.AttendanceStatus;
import com.hongik.devtalk.domain.enums.StudentStatus;
import com.hongik.devtalk.domain.seminar.CustomSeminarApplicantErrorCode;
import com.hongik.devtalk.domain.seminar.dto.ApplicantRequestDto;
import com.hongik.devtalk.domain.seminar.dto.ApplicantResponseDto;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.ApplicantRepository;
import com.hongik.devtalk.repository.AttendanceRepository;
import com.hongik.devtalk.repository.QuestionRepository;
import com.hongik.devtalk.repository.SessionRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import com.hongik.devtalk.repository.seminar.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeminarApplicantService {
    private final StudentRepository studentRepository;
    private final SeminarRepository seminarRepository;
    private final ApplicantRepository applicantRepository;
    private final QuestionRepository questionRepository;
    private final SessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;

    public ApiResponse<ApplicantResponseDto> createApplicant(ApplicantRequestDto applicantRequestDto) {
        //학생 정보 확인
        Student student = studentRepository.findByStudentNum(applicantRequestDto.getStudentNum())
                .orElseGet(() -> Student.builder()
                        .studentNum(applicantRequestDto.getStudentNum())
                        .status(StudentStatus.ACTIVE)
                        .build());

        // 학생 정보 업데이트 (기존 학생이든 새로운 학생이든 모두 적용)
        student.updateDetails(
                applicantRequestDto.getName(),
                applicantRequestDto.getPhone(),
                applicantRequestDto.getGrade(),
                applicantRequestDto.getGradeEtc(),
                applicantRequestDto.getDepartments(),
                applicantRequestDto.getDepartmentEtc(),
                applicantRequestDto.getEmail()
        );
        studentRepository.save(student);

        //현재 신청 가능한 세미나
        Seminar seminar = seminarRepository.findSeminarInApplicationPeriod(LocalDateTime.now());
        if (seminar == null) {
            throw new GeneralException(CustomSeminarApplicantErrorCode.SEMINAR_APPLICANT_ERROR);
        }

        if (applicantRepository.existsByStudentAndSeminar(student, seminar)) {
            throw new GeneralException(CustomSeminarApplicantErrorCode.ALREADY_APPLIED_SEMINAR);
        }

        Applicant applicant = Applicant.builder()
                .student(student)
                .seminar(seminar)
                .inflowPath(applicantRequestDto.getInflowPath())
                .inflowPathEtc(applicantRequestDto.getInflowPathEtc())
                .participationType(applicantRequestDto.getParticipationType())
                .build();

        applicantRepository.save(applicant);

        //질문 저장
        if (applicantRequestDto.getQuestions() != null && !applicantRequestDto.getQuestions().isEmpty()) {
            List<Question> questionsToSave = applicantRequestDto.getQuestions().stream()
                    .map(questionDto -> {
                        // [수정 2] .get() 대신 .orElseThrow()를 사용하고, 올바른 Enum으로 예외를 던집니다.
                        Session session = sessionRepository.findById(questionDto.getSessionId())
                                .orElseThrow(() -> new GeneralException(CustomSeminarApplicantErrorCode.SESSION_NOT_FOUND));

                        return Question.builder()
                                .content(questionDto.getContent())
                                .student(student)
                                .session(session)
                                .build();
                    }).toList();

            questionRepository.saveAll(questionsToSave);
        }

        Attendance initialAttendance = Attendance.builder()
                .applicant(applicant)
                .seminar(seminar)
                .status(AttendanceStatus.ABSENT) // 기본 상태는 '결석'
                .build();
        attendanceRepository.save(initialAttendance);

        ApplicantResponseDto applicantResponseDto = ApplicantResponseDto.builder()
                .studentId(student.getId())
                .applicantId(applicant.getId())
                .seminarId(seminar.getId())
                .build();

        return ApiResponse.onSuccess("성공적으로 신청이 완료되었습니다.",applicantResponseDto);
    }
}
