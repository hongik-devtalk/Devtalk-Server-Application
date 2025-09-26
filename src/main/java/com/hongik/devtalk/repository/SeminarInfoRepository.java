package com.hongik.devtalk.repository;


import com.hongik.devtalk.domain.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeminarInfoRepository extends JpaRepository<Seminar,Long> {
}
