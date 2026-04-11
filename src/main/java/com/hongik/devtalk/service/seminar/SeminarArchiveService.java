package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.*;
import com.hongik.devtalk.domain.enums.AttendanceStatus;
import com.hongik.devtalk.domain.live.CustomLiveErrorCode;
import com.hongik.devtalk.domain.live.LiveError;
import com.hongik.devtalk.domain.live.dto.ReviewResponseDto;
import com.hongik.devtalk.domain.seminar.dto.SeminarArchiveReviewRequestDto;
import com.hongik.devtalk.domain.seminar.dto.SeminarArchiveReviewResponseDto;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.ApplicantRepository;
import com.hongik.devtalk.repository.AttendanceRepository;
import com.hongik.devtalk.repository.review.ReviewRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import com.hongik.devtalk.repository.seminar.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SeminarArchiveService {

    private final StudentRepository studentRepository;
    private final ApplicantRepository applicantRepository;
    private final SeminarRepository seminarRepository;
    private final ReviewRepository reviewRepository;
    private final AttendanceRepository attendanceRepository;

    //세미나 아카이브 리뷰 작성
    public ApiResponse<SeminarArchiveReviewResponseDto> createArchiveReview(String studentNum,Long seminarId, SeminarArchiveReviewRequestDto requestDto) {

        //학생이 존재하는지 확인
        Optional<Student> optionalStudent = studentRepository.findByStudentNum(studentNum);

        if(optionalStudent.isEmpty()) {
            return ApiResponse.onFailure(GeneralErrorCode.FORBIDDEN, LiveError.STUDENT_NOT_FOUND);

        }

        //student 객체가 있으면 꺼내기
        Student student = optionalStudent.get();

        //세미나 존재 여부 확인
        Seminar seminar = seminarRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));


        //학생이 해당 세미나를 들었는지 조회
        Applicant applicant = applicantRepository.findBySeminarAndStudent(seminar,student)
                .orElseThrow(()->new GeneralException(GeneralErrorCode.APPLICANT_NOT_FOUND));


        //출석 여부 확인
        Attendance attendance = attendanceRepository.findByApplicantAndSeminar(applicant,seminar)
                .orElseThrow(()->new GeneralException(GeneralErrorCode.ATTENDANCE_REQUIRED));


        if(attendance.getStatus() == AttendanceStatus.ABSENT) {
            throw new GeneralException(CustomLiveErrorCode.ATTEND_ABSENT,"세미나에 출석한 학생만 리뷰작성이 가능합니다.");
        }

        //리뷰 중복 작성 확인

        if(reviewRepository.existsReviewByStudentAndSeminar(student, seminar)) {
            throw new GeneralException(CustomLiveErrorCode.REVIEW_DUPLICATE_ERROR,"리뷰는 세미나당 1회만 작성가능합니다.");
        }


        Review review = Review.builder()
                .student(student)
                .seminar(seminar)
                .totalContent(requestDto.getTotalContent())
                .score(requestDto.getScore())
                .isPublic(true)
                .build();

        //리뷰 저장
        Review savedReview = reviewRepository.save(review);


        SeminarArchiveReviewResponseDto responseDto = SeminarArchiveReviewResponseDto.builder()
                .reviewId(savedReview.getId())
                .studentNum(student.getStudentNum())
                .seminarId(seminar.getId())
                .seminarNum(seminar.getSeminarNum())
                .description(seminar.getDescription())
                .score(savedReview.getScore())
                .totalContent(savedReview.getTotalContent())
                .build();

    return ApiResponse.onSuccess("성공적으로 리뷰가 등록되었습니다.",responseDto);

    }
}
