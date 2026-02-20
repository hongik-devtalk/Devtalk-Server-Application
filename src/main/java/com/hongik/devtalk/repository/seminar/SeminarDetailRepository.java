package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeminarDetailRepository extends JpaRepository<Seminar, Long> {

    //세미나 키워드 검색용
        // SELECT * FROM seminar WHERE topic LIKE %:keyword%
        List<Seminar> findByTopicContaining(String keyword);

}
