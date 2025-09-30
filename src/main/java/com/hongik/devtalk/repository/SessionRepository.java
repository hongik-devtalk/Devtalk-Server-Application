package com.hongik.devtalk.repository;

import com.hongik.devtalk.domain.Seminar;
import com.hongik.devtalk.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findSessionsBySeminar(Seminar seminar);

}
