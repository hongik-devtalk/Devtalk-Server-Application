package com.hongik.devtalk.repository.mainpage;

import com.hongik.devtalk.domain.InquiryLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InquiryLinkRepository extends JpaRepository<InquiryLink, String> {
    
    // 기본적으로 하나의 레코드만 존재한다고 가정
    Optional<InquiryLink> findTopByOrderById();
}
