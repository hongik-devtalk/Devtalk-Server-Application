package com.hongik.devtalk.domain;

import com.hongik.devtalk.domain.enums.AttendanceStatus;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant;

    // 세미나 별로 출석체크
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id", nullable = false)
    private Seminar seminar;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    private LocalDateTime checkInTime;

    public void updateAttendance(AttendanceStatus status, LocalDateTime checkInTime) {

        if (this.status == status) {
            if (status == AttendanceStatus.PRESENT) {
                throw new GeneralException(GeneralErrorCode.ALREADY_PRESENT);
            } else if (status == AttendanceStatus.ABSENT) {
                throw new GeneralException(GeneralErrorCode.ALREADY_ABSENT);
            }
        }

        this.status = status;
        this.checkInTime = checkInTime;
    }
}
