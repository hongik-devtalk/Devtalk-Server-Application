package com.hongik.devtalk.repository.mainpage;

import com.hongik.devtalk.domain.MainpageImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MainpageImagesRepository extends JpaRepository<MainpageImages, String> {
    
    // 기본적으로 하나의 레코드만 존재한다고 가정
    Optional<MainpageImages> findTopByOrderById();
}
