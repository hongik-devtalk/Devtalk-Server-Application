package com.hongik.devtalk.repository;

import com.hongik.devtalk.domain.Applicant;
import com.hongik.devtalk.domain.Attendance;
import com.hongik.devtalk.domain.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    public Optional<Attendance> findByApplicantAndSeminar(Applicant applicant, Seminar seminar);
}
