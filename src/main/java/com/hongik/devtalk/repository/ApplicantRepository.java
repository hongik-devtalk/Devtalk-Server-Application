package com.hongik.devtalk.repository;

import com.hongik.devtalk.domain.Applicant;
import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant,Long> {
    /**
     *  학생의 신청 기록 중, 세미나 날짜가 가장 최신인 신청 정보를 찾습니다.
     * @param student 학생 엔티티
     * @return Optional<Applicant>
     */
    Applicant findFirstByStudentOrderBySeminar_SeminarDateDesc(Student student);

    //학생과 세미나 정보로 신청 내역이 있는지 확입합니다.
    boolean existsByStudentAndSeminar(Student student, Seminar seminar);
}
