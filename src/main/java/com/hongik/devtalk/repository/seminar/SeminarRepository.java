package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeminarRepository extends JpaRepository<Seminar,Long> {
    List<Seminar> findAllByOrderBySeminarNumDesc();
}
