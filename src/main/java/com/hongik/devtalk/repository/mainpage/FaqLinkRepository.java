package com.hongik.devtalk.repository.mainpage;

import com.hongik.devtalk.domain.FaqLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaqLinkRepository extends JpaRepository<FaqLink, String> {
    
    // 기본적으로 하나의 레코드만 존재한다고 가정
    Optional<FaqLink> findTopByOrderById();
}

