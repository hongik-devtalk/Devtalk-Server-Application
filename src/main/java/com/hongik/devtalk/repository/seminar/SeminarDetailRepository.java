package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeminarDetailRepository extends JpaRepository<Seminar, Long> {
}
