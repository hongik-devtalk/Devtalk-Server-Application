package com.hongik.devtalk.service.live;

import com.hongik.devtalk.domain.*;
import com.hongik.devtalk.domain.enums.AttendanceStatus;
import com.hongik.devtalk.domain.live.CustomLiveErrorCode;
import com.hongik.devtalk.domain.live.LiveError;
import com.hongik.devtalk.domain.live.dto.*;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.global.security.JwtTokenProvider;
import com.hongik.devtalk.repository.ApplicantRepository;
import com.hongik.devtalk.repository.AttendanceRepository;
import com.hongik.devtalk.repository.review.ReviewRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import com.hongik.devtalk.repository.seminar.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//학생 인증, 라이브 입장(출석체크), 리뷰 작성(10일 기간)
@Service
@RequiredArgsConstructor
@Slf4j
public class LiveService {
    private final SeminarRepository seminarRepository;
    private final StudentRepository studentRepository;
    private final ApplicantRepository applicantRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider tokenProvider;
    private final ReviewRepository reviewRepository;
    private final AttendanceRepository attendanceRepository;

    //학생 인증 (이 학생이 현재 진행되고 있는 세미나를 신청했는지 확인하는 로직)
    @Transactional
    public ApiResponse<AuthStudentResponseDto> authStudent(AuthStudentRequestDto authStudentRequestDto) {
        Optional<Student> optionalStudent = studentRepository.findByStudentNum(authStudentRequestDto.getStudentNum());
        if (optionalStudent.isEmpty()) {
            // 비어있다면 실패 응답을 반환하고 메서드를 종료합니다.
            return ApiResponse.onFailure(GeneralErrorCode.FORBIDDEN, LiveError.STUDENT_NOT_FOUND);
        }
        Student student = optionalStudent.get();

        //학생이 신청한 세미나중 가장 최근 세미나를 가져옴
        Applicant latestApplicant = applicantRepository.findFirstByStudentOrderBySeminar_SeminarDateDesc(student);
        if(latestApplicant == null) {return ApiResponse.onFailure(GeneralErrorCode.FORBIDDEN,LiveError.APPLICANT_NOT_FOUND);}

        Seminar seminar = latestApplicant.getSeminar();

        LocalDate seminarDate = seminar.getSeminarDate().toLocalDate();
        LocalDate deadline = seminarDate.plusDays(10); // 세미나 날짜 + 10일
        LocalDate today = LocalDate.now();

        //세미나 인증 가능 기간 확인 (세미나 당일부터 10일후까지 가능)
        if(today.isBefore(seminarDate) || today.isAfter(deadline)) {
            return ApiResponse.onFailure(CustomLiveErrorCode.SEMINAR_TIME_ERROR,LiveError.SEMINAR_NOT_FOUND);
        } else {
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            Authentication authentication = new UsernamePasswordAuthenticationToken(student.getStudentNum(), null, authorities);


            // 3. JwtTokenProvider를 사용해 Access Token 생성
            String accessToken = tokenProvider.generateToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(authentication);

            // 4. 응답 DTO를 생성하여 성공 응답 반환
            AuthStudentResponseDto responseDto = AuthStudentResponseDto.builder()
                    .studentId(student.getId())
                    .seminarId(seminar.getId())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();

            return ApiResponse.onSuccess("신청자 인증에 성공하였습니다.", responseDto);
        }
    }

    //라이브 출석 체크
    @Transactional
    public ApiResponse<AttendanceResponseDto> attendanceCheck(String studentNum, LocalDateTime attendTime) {
        Optional<Student> optionalStudent = studentRepository.findByStudentNum(studentNum);
        if (optionalStudent.isEmpty()) {
            // 비어있다면 실패 응답을 반환하고 메서드를 종료합니다.
            return ApiResponse.onFailure(GeneralErrorCode.FORBIDDEN, LiveError.STUDENT_NOT_FOUND);
        }

        Student student = optionalStudent.get();

        Applicant latestApplicant = applicantRepository.findFirstByStudentOrderBySeminar_SeminarDateDesc(student);
        if(latestApplicant == null) {return ApiResponse.onFailure(GeneralErrorCode.FORBIDDEN,LiveError.APPLICANT_NOT_FOUND);}

        Seminar seminar = latestApplicant.getSeminar();
        if(seminar.getLive() == null || seminar.getLive().getLiveUrl() == null) {
            return ApiResponse.onFailure(CustomLiveErrorCode.LIVEURL_NOT_FOUND, LiveError.LIVEURL_NOT_FOUND);
        }

        Attendance attendance = attendanceRepository.findByApplicantAndSeminar(latestApplicant, seminar)
                .orElseThrow(() -> new GeneralException(CustomLiveErrorCode.APPLICANT_NOT_FOUND, "신청 정보를 찾을 수 없습니다."));

        // 3. 이미 출석(PRESENT) 또는 지각(LATE) 처리된 경우, 상태를 바꾸지 않고 바로 반환합니다.
        if (attendance.getStatus() != AttendanceStatus.ABSENT) {
            AttendanceResponseDto responseDto = AttendanceResponseDto.builder()
                    .liveUrl(seminar.getLive().getLiveUrl())
                    .attendanceStatus(attendance.getStatus()) // 기존 상태를 그대로 반환
                    .build();
            return ApiResponse.onSuccess("이미 출석 처리된 사용자입니다.", responseDto);
        }

        LocalDateTime realSeminarTime = seminar.getSeminarDate(); // 실제 세미나 시작 시간
        LocalDateTime checkInStartTime = realSeminarTime.minusMinutes(10); // 출석 체크 시작 시간 (시작 10분 전)
        LocalDateTime onTimeDeadline = realSeminarTime.plusMinutes(80);   // 출석(PRESENT) 마감 시간 (시작 80분 후)

        // 5. 출석 상태 결정
        AttendanceStatus newStatus;
        if (attendTime.isBefore(checkInStartTime)) {
            // 출석 체크 시작 시간보다 이전인 경우
            return ApiResponse.onFailure(CustomLiveErrorCode.ATTENDANCE_NOT_YET_OPEN, "아직 출석 체크 시간이 아닙니다.");
        } else if (!attendTime.isAfter(onTimeDeadline)) {
            // 출석 마감 시간(onTimeDeadline) 이후가 아닌 경우 (즉, 마감 시간과 같거나 이전인 경우)
            newStatus = AttendanceStatus.PRESENT;
        } else {
            // 그 외 모든 경우 (출석 마감 시간 이후)
            newStatus = AttendanceStatus.LATE;
        }

        // 6. 조회한 엔티티의 상태와 체크인 시간을 '수정'합니다.
        attendance.updateAttendance(newStatus, attendTime);

        // 7. 최종 응답 반환
        AttendanceResponseDto responseDto = AttendanceResponseDto.builder()
                .liveUrl(seminar.getLive().getLiveUrl())
                .attendanceStatus(newStatus) // 새로 변경된 상태를 반환
                .build();

        return ApiResponse.onSuccess("성공적으로 출석 인증 되었습니다.", responseDto);
    }

    //리뷰 작성
    @Transactional
    public ApiResponse<ReviewResponseDto> createReview(String studentNum,ReviewRequestDto reviewRequestDto) {
        Optional<Student> optionalStudent = studentRepository.findByStudentNum(studentNum);
        if (optionalStudent.isEmpty()) {
            // 비어있다면 실패 응답을 반환하고 메서드를 종료합니다.
            return ApiResponse.onFailure(GeneralErrorCode.FORBIDDEN, LiveError.STUDENT_NOT_FOUND);
        }

        Student student = optionalStudent.get();

        Applicant latestApplicant = applicantRepository.findFirstByStudentOrderBySeminar_SeminarDateDesc(student);
        if(latestApplicant == null) {return ApiResponse.onFailure(GeneralErrorCode.FORBIDDEN,LiveError.APPLICANT_NOT_FOUND);}

        Seminar seminar = latestApplicant.getSeminar();

        LocalDate seminarDate = seminar.getSeminarDate().toLocalDate();
        LocalDate deadline = seminarDate.plusDays(10);
        LocalDate today = LocalDate.now();

        // 리뷰 작성 가능 기간 확인
        if(today.isBefore(seminarDate) || today.isAfter(deadline)) {
            // "리뷰 작성 기간이 아닙니다"와 같은 에러 응답 반환
            return ApiResponse.onFailure(CustomLiveErrorCode.REVIEW_PERIOD_INVALID, LiveError.REVIEW_PERIOD_INVALID);
        }

        Review review = Review.builder()
                .student(student)
                .seminar(seminar)
                .strength(reviewRequestDto.getStrength())
                .improvement(reviewRequestDto.getImprovement())
                .score(reviewRequestDto.getScore())
                .nextTopic(reviewRequestDto.getNextTopic())
                .isPublic(reviewRequestDto.isPublic())
                .build();

        Review savedReview = reviewRepository.save(review);

        ReviewResponseDto responseDto = ReviewResponseDto.builder()
                .reviewId(savedReview.getId())
                .studentNum(student.getStudentNum())
                .seminarNum(seminar.getSeminarNum())
                .build();

        return ApiResponse.onSuccess("성공적으로 리뷰가 등록되었습니다.",responseDto);

    }

}
