package com.hongik.devtalk.repository;

import com.hongik.devtalk.domain.SeminarTag;
import com.hongik.devtalk.domain.SpeakerTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpeakerTagRepository extends JpaRepository<SpeakerTag, Long> {
    List<SpeakerTag> findByTag_TagTextIgnoreCase(String tagText);
}