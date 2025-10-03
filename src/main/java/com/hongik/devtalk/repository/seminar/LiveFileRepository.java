package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.LiveFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiveFileRepository extends JpaRepository<LiveFile, Long> {
}
