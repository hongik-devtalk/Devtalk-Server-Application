package com.hongik.devtalk.repository.review;

import com.hongik.devtalk.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    // 모든 후기 조회 (displayOrder 오름차순, null은 마지막)
    @Query("SELECT r FROM Review r ORDER BY CASE WHEN r.displayOrder IS NULL THEN 1 ELSE 0 END, r.displayOrder ASC, r.createdAt DESC")
    List<Review> findAllOrderByDisplayOrder();
    
    // 홈 화면에 표시되는 후기 조회 (isNote = true)
    List<Review> findByIsNoteTrue();
}
