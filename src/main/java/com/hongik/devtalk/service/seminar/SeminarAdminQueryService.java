package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.*;
import com.hongik.devtalk.domain.seminar.admin.dto.ApplicantResponseDTO;
import com.hongik.devtalk.domain.seminar.admin.dto.QuestionResponseDTO;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarInfoResponseDTO;
import com.hongik.devtalk.domain.seminar.admin.dto.SeminarNumResponseDTO;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.ApplicantRepository;
import com.hongik.devtalk.repository.QuestionRepository;
import com.hongik.devtalk.repository.SessionRepository;
import com.hongik.devtalk.repository.live.LiveRepository;
import com.hongik.devtalk.repository.liveFile.LiveFileRepository;
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
    private final QuestionRepository questionRepository;
    private final LiveRepository liveRepository;
    private final LiveFileRepository liveFileRepository;
    private final SessionRepository sessionRepository;

    /**
     * 세미나 ID로 특정 세미나의 신청자 목록을 조회
     *
     * @param seminarId 세미나 ID
     * @return 신청자 정보 DTO 리스트
     * @throws GeneralException 세미나가 존재하지 않을 경우
     */
    public List<ApplicantResponseDTO> getApplicants(Long seminarId) {
        // 세미나 존재 여부 확인
        Seminar seminar = seminarRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

        String topic = seminar.getTopic();

        // 세미나 신청자 목록 조회
        List<Applicant> applicants = applicantRepository.findApplicantsBySeminarId(seminarId);

        return applicants.stream()
                .map(applicant -> ApplicantResponseDTO.from(applicant, topic))
                .toList();
    }

    /**
     * 세미나 ID로 특정 세미나의 학생별 질문 목록을 조회
     *
     * @param seminarId 세미나 ID
     * @return 학생별 질문 정보 DTO 리스트
     * @throws GeneralException 세미나가 존재하지 않을 경우
     */
    public QuestionResponseDTO getQuestions(Long seminarId) {
        // 세미나 존재 여부 확인
        Seminar seminar = seminarRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

        // 세미나 → 세션 → 연사 목록 추출
        List<QuestionResponseDTO.SpeakerDTO> speakers = seminar.getSessions().stream()
                .map(Session::getSpeaker)
                .map(speaker -> QuestionResponseDTO.SpeakerDTO.builder()
                        .speakerId(speaker.getId())
                        .speakerName(speaker.getName())
                        .build())
                .toList();

        // 질문 조회
        List<Question> questions = questionRepository.findQuestionsBySeminarId(seminarId);

        // 학생별 질문 그룹핑 후 DTO 변환
        List<QuestionResponseDTO.StudentQuestionDTO> students = questions.stream()
                .collect(Collectors.groupingBy(q -> q.getStudent().getId()))
                .values().stream()
                .map(studentQuestions -> QuestionResponseDTO.StudentQuestionDTO.from(
                        studentQuestions.get(0).getStudent(), studentQuestions
                ))
                .toList();

        return QuestionResponseDTO.builder()
                .speakers(speakers)
                .students(students)
                .build();

    }

    /**
     * 세미나 ID로 특정 세미나의 상세 정보를 조회합니다.
     *
     * @param seminarId 세미나 ID
     * @return 세미나 상세 정보 DTO
     * @throws GeneralException 세미나가 존재하지 않을 경우
     */
    public SeminarInfoResponseDTO getSeminarInfo(Long seminarId) {
        // 세미나 존재 여부 확인
        Seminar seminar = seminarRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));

        // DTO 변환
        Live live = liveRepository.findBySeminarId(seminarId).orElse(null);
        List<LiveFile> materials = liveFileRepository.findBySeminarId(seminarId);
        List<Session> sessions = sessionRepository.findBySeminarId(seminarId);

        return SeminarInfoResponseDTO.from(seminar, live, materials, sessions);
    }

    /**
     * 현재 등록되어 있는 모든 세미나의 회차 번호 리스트를 조회
     *
     * @return 세미나 회차 번호(Integer) 리스트 DTO
     */
    public SeminarNumResponseDTO getSeminarNums() {
        List<Integer> seminarNums = seminarRepository.findAllSeminarNums();
        return SeminarNumResponseDTO.builder()
                .seminarNums(seminarNums)
                .build();
    }
}
