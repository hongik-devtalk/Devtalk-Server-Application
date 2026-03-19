package com.hongik.devtalk.repository;

import com.hongik.devtalk.domain.SeminarTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeminarTagRepository extends JpaRepository<SeminarTag, Long> {
    List<SeminarTag> findByTag_TagTextIgnoreCase(String tagText);
}