package com.hongik.devtalk.repository;

import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.Session;
import com.hongik.devtalk.domain.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findSessionsBySeminar(Seminar seminar);
    Optional<Session> findBySeminarAndSpeaker(Seminar seminar, Speaker speaker);
    List<Session> findBySeminarId(Long seminarId);

    @Query("""
            select s
            from Session s
            join fetch s.speaker sp
            where s.seminar.id = :seminarId
            order by s.id asc
            """)
    List<Session> findBySeminarIdWithSpeaker(@Param("seminarId") Long seminarId);
}
