package com.hongik.devtalk.repository;

import com.hongik.devtalk.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select distinct q from Question q " +
            "join fetch q.student s " +
            "join fetch q.session ss " +
            "join fetch ss.speaker sp " +
            "where ss.seminar.id = :seminarId")
    List<Question> findQuestionsBySeminarId(Long seminarId);
}
