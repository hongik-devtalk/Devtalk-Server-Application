package com.hongik.devtalk.repository.speaker;

import com.hongik.devtalk.domain.Speaker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
}
