package com.hongik.devtalk.repository.userhome;


import com.hongik.devtalk.domain.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeminarInfoRepository extends JpaRepository<Seminar,Long> {
}
