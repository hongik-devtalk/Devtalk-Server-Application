package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.Applicant;
import com.hongik.devtalk.domain.Question;
import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.Student;
import com.hongik.devtalk.domain.enums.InflowPath;
import com.hongik.devtalk.domain.enums.ParticipationType;
import com.hongik.devtalk.domain.enums.StudentStatus;
import com.hongik.devtalk.domain.seminar.dto.ApplicantRequestDto;
import com.hongik.devtalk.domain.seminar.dto.ApplicantResponseDto;
import com.hongik.devtalk.repository.ApplicantRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import com.hongik.devtalk.repository.seminar.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeminarApplicantService {

    private final SeminarRepository seminarRepository;
    private final ApplicantRepository applicantRepository;
    private final StudentRepository studentRepository;


    //세미나 신청하기
    public ApplicantResponseDto createSeminarApply(ApplicantRequestDto applicantRequestDto,Long seminarId) {

        //세미나 정보확인
        Seminar seminar =seminarRepository.findById(seminarId)
                .orElseThrow(()->new IllegalArgumentException("해당 세미나가 존재하지 않습니다. "));
        //학생 정보 확인 및 없다면 생성
        Student student = studentRepository.findByStudentNum(applicantRequestDto.getStudentNum())
                .orElseGet(()->
                {Student newStudent = Student.builder()
                        .studentNum(applicantRequestDto.getStudentNum())
                        .name(applicantRequestDto.getName())
                        .grade(applicantRequestDto.getGrade())
                        .phone(applicantRequestDto.getPhone())
                        .status(StudentStatus.ACTIVE) // 예시: 기본 상태 'ACTIVE'
                        .build();
        return studentRepository.save(newStudent);}
                        );
        //신청된 세미나- 학생 확인

        if(applicantRepository.existsBySeminarAndStudent(seminar,student)){
            throw new IllegalArgumentException("이미 신청된 세미나입니다.");
        }

        //Applicant 생성
        Applicant applicant = Applicant.builder()
                .seminar(seminar)
                .student(student)
                .participationType(ParticipationType.valueOf(applicantRequestDto.getParticipationType()))
                .inflowPath(InflowPath.valueOf(applicantRequestDto.getInflowPath()))
                .build();

        //question 엔티티 생성 및 매핑
        List<Question> questions = applicantRequestDto.getQuestions().stream()
                .map(qDto -> Question.builder() // 2. 빌더 패턴으로 생성
                        .content(qDto.getQuestion())
                        .build())
                .collect(Collectors.toList());




        Applicant savedApplicant = applicantRepository.save(applicant);

        return ApplicantResponseDto.from(savedApplicant);

    }
}
