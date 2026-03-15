package com.hongik.devtalk.repository;

import com.hongik.devtalk.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagTextIgnoreCase(String tagText);
    List<Tag> findTop3ByOrderBySearchCountDesc();
}