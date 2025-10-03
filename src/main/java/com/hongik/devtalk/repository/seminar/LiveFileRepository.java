package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.LiveFile;
import com.hongik.devtalk.domain.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LiveFileRepository extends JpaRepository<LiveFile, Long> {
    List<LiveFile> findBySeminar(Seminar seminar);
}
