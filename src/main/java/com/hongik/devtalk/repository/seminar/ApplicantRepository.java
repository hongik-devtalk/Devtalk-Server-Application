package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    @Query("select distinct a from Applicant a " +
            "join fetch a.student s " +
            "join fetch s.studentDepartments sd " +
            "join fetch sd.department d " +
            "join fetch a.seminar se " +
            "where se.id = :seminarId")
    List<Applicant> findApplicantsBySeminarId(Long seminarId);
}