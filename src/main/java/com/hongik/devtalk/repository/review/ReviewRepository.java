package com.hongik.devtalk.repository.review;

import com.hongik.devtalk.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
