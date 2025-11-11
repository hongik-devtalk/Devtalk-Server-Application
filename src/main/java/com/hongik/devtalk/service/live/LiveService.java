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
import com.hongik.devtalk.repository.auth.RefreshTokenRepository;
import com.hongik.devtalk.repository.review.ReviewRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import com.hongik.devtalk.repository.seminar.ShowSeminarRepository;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final ShowSeminarRepository showSeminarRepository;

    //학생 인증 (이 학생이 현재 진행되고 있는 세미나를 신청했는지 확인하는 로직)
    @Transactional
    public ApiResponse<AuthStudentResponseDto> authStudent(AuthStudentRequestDto authStudentRequestDto) {
        Optional<Student> optionalStudent = studentRepository.findByStudentNumAndName(authStudentRequestDto.getStudentNum(),authStudentRequestDto.getName());
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
        LocalDate seminarDateMinus = seminarDate.minusDays(1);
        LocalDate deadline = seminarDate.plusDays(10); // 세미나 날짜 + 10일
        LocalDate today = LocalDate.now();

        //세미나 인증 가능 기간 확인 (세미나 당일부터 10일후까지 가능)
        if(today.isBefore(seminarDateMinus) || today.isAfter(deadline)) {
            return ApiResponse.onFailure(CustomLiveErrorCode.SEMINAR_TIME_ERROR,LiveError.SEMINAR_NOT_FOUND);
        } else {
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            Authentication authentication = new UsernamePasswordAuthenticationToken(student.getStudentNum(), null, authorities);


            // 3. JwtTokenProvider를 사용해 Access Token 생성
            String accessToken = tokenProvider.generateToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(authentication);

            refreshTokenRepository.findByStudentId(student.getId())
                    .ifPresentOrElse(
                            token -> token.setToken(refreshToken), // 이미 토큰이 있으면 새 토큰으로 교체 (업데이트)
                            () -> refreshTokenRepository.save(new RefreshToken(student.getId(), refreshToken)) // 없으면 새로 저장 (생성)
                    );

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

    @Transactional
    public ReissueResponseDto reissueToken(ReissueRequestDto reissueRequestDto) {
        // 1. Refresh Token 유효성 검증
        if (!tokenProvider.validateToken(reissueRequestDto.getRefreshToken())) {
            throw new GeneralException(GeneralErrorCode.INVALID_TOKEN);
        }

        // 2. DB에서 Refresh Token 조회
        RefreshToken token = refreshTokenRepository.findByToken(reissueRequestDto.getRefreshToken())
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.INVALID_TOKEN, "DB에 존재하지 않는 리프레시 토큰입니다."));

        // 3. 토큰에 연결된 학생 정보 조회
        Student student = studentRepository.findById(token.getStudentId())
                .orElseThrow(() -> new GeneralException(CustomLiveErrorCode.STUDENT_NOT_FOUND));

        // 4. 새로운 토큰 생성 (Access & Refresh 둘 다 - RTR 적용)
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(student.getStudentNum(), null, authorities);

        String newAccessToken = tokenProvider.generateToken(authentication);
        String newRefreshToken = tokenProvider.generateRefreshToken(authentication);

        // 5. DB에 있는 기존 Refresh Token을 새로운 Refresh Token으로 업데이트
        token.setToken(newRefreshToken);

        // 6. 새로운 토큰들을 DTO에 담아 반환
        return ReissueResponseDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
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

        Seminar liveSeminar = showSeminarRepository.findFirstByLiveActivateTrue()
                .map(ShowSeminar::getSeminar)
                .orElseThrow(() -> new GeneralException(CustomLiveErrorCode.SEMINAR_NOT_FOUND, "현재 출석체크가 가능한 세미나가 없습니다."));

        if (liveSeminar == null) {
            throw new GeneralException(CustomLiveErrorCode.SEMINAR_NOT_FOUND, "현재 진행중인 세미나가 없습니다.");
        }

        if(liveSeminar.getLive().getLiveUrl() == null){
            throw new GeneralException(CustomLiveErrorCode.LIVE_URL_NOT_FOUND,"현재 라이브 URL이 등록되지 않았습니다.");
        }

        Applicant latestApplicant = applicantRepository.findFirstByStudentOrderBySeminar_SeminarDateDesc(student);
        if(latestApplicant == null) {return ApiResponse.onFailure(GeneralErrorCode.FORBIDDEN,LiveError.APPLICANT_NOT_FOUND);}


        Attendance attendance = attendanceRepository.findByApplicantAndSeminar(latestApplicant, liveSeminar)
                .orElseThrow(() -> new GeneralException(CustomLiveErrorCode.APPLICANT_NOT_FOUND, "신청 정보를 찾을 수 없습니다."));

        // 3. 이미 출석(PRESENT) 또는 지각(LATE) 처리된 경우, 상태를 바꾸지 않고 바로 반환합니다.
        if (attendance.getStatus() != AttendanceStatus.ABSENT) {
            AttendanceResponseDto responseDto = AttendanceResponseDto.builder()
                    .liveUrl(liveSeminar.getLive().getLiveUrl())
                    .attendanceStatus(attendance.getStatus()) // 기존 상태를 그대로 반환
                    .build();
            return ApiResponse.onSuccess("이미 출석 처리된 사용자입니다.", responseDto);
        }

        LocalDateTime realSeminarTime = liveSeminar.getSeminarDate(); // 실제 세미나 시작 시간
        LocalDateTime checkInStartTime = realSeminarTime.minusMinutes(70); // 출석 체크 시작 시간 (시작 10분 전)
        LocalDateTime onTimeDeadline = realSeminarTime.plusMinutes(80);   // 출석(PRESENT) 마감 시간 (시작 80분 후)

        // 5. 출석 상태 결정
        AttendanceStatus newStatus;
        if (attendTime.isBefore(checkInStartTime)) {
            //출석 체크 시작 시간보다 이전인 경우
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
                .liveUrl(liveSeminar.getLive().getLiveUrl())
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

        Attendance attendance = attendanceRepository.findByApplicantAndSeminar(latestApplicant, seminar)
                .orElseThrow(() -> new GeneralException(CustomLiveErrorCode.APPLICANT_NOT_FOUND, "신청 정보를 찾을 수 없습니다."));

        if(attendance.getStatus() == AttendanceStatus.ABSENT) {
            throw new GeneralException(CustomLiveErrorCode.ATTEND_ABSENT,"세미나에 출석한 학생만 리뷰작성이 가능합니다.");
        }

        LocalDate seminarDate = seminar.getSeminarDate().toLocalDate();
        LocalDate deadline = seminarDate.plusDays(10);
        LocalDate today = LocalDate.now();

        // 리뷰 작성 가능 기간 확인
        if(today.isBefore(seminarDate) || today.isAfter(deadline)) {
            // "리뷰 작성 기간이 아닙니다"와 같은 에러 응답 반환
            return ApiResponse.onFailure(CustomLiveErrorCode.REVIEW_PERIOD_INVALID, LiveError.REVIEW_PERIOD_INVALID);
        }
        if(reviewRepository.existsReviewByStudentAndSeminar(student, seminar)) {
            throw new GeneralException(CustomLiveErrorCode.REVIEW_DUPLICATE_ERROR,"리뷰는 세미나당 1회만 작성가능합니다.");
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
                .seminarId(seminar.getId())
                .seminarNum(seminar.getSeminarNum())
                .isPublic(review.isPublic())
                .build();

        return ApiResponse.onSuccess("성공적으로 리뷰가 등록되었습니다.",responseDto);

    }

}
