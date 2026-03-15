package com.hongik.devtalk.repository.speaker;

import com.hongik.devtalk.domain.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
    //연사 키워드 검색용
    List<Speaker> findByNameContaining(String keyword);

}
