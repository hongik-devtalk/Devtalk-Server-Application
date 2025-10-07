package com.hongik.devtalk.repository.review;

import com.hongik.devtalk.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    // 공개된 후기만 조회 (isPublic = true, displayOrder 오름차순, null은 마지막)
    @Query("SELECT r FROM Review r WHERE r.isPublic = true ORDER BY CASE WHEN r.displayOrder IS NULL THEN 1 ELSE 0 END, r.displayOrder ASC, r.createdAt DESC")
    List<Review> findAllOrderByDisplayOrder();
    
    // 홈 화면에 표시되는 후기 조회 (isNote = true, isPublic = true)
    List<Review> findByIsNoteTrueAndIsPublicTrue();

    // 특정 세미나에 대한 후기 + 학생 정보 조회
    @Query("select distinct r from Review r " +
            "join fetch r.student s " +
            "left join fetch s.departments " +
            "where r.seminar.id = :seminarId")
    List<Review> findBySeminarIdWithStudentAndDepartments(@Param("seminarId") Long seminarId);
}
