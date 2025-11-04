package com.hongik.devtalk.service.mainpage;

import com.hongik.devtalk.domain.FaqLink;
import com.hongik.devtalk.domain.mainpage.dto.FaqLinkResponseDto;
import com.hongik.devtalk.domain.mainpage.dto.FaqLinkRequestDto;
import com.hongik.devtalk.domain.mainpage.dto.DeleteLinkResponseDto;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.mainpage.FaqLinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaqLinkService {

    private final FaqLinkRepository faqLinkRepository;
    
    private static final String FAQ_LINK_ID = "faq_link_1"; // 고정 ID

    /**
     * FAQ 링크를 조회합니다.
     */
    public FaqLinkResponseDto getFaqLink() {
        Optional<FaqLink> faqLinkOpt = faqLinkRepository.findTopByOrderById();
        
        if (faqLinkOpt.isEmpty()) {
            // 데이터가 없으면 기본값 반환
            return FaqLinkResponseDto.builder()
                    .linkId(null)
                    .url(null)
                    .updatedAt(null)
                    .updatedBy(null)
                    .build();
        }

        FaqLink faqLink = faqLinkOpt.get();
        
        return FaqLinkResponseDto.builder()
                .linkId(faqLink.getId())
                .url(faqLink.getUrl())
                .updatedAt(faqLink.getUpdatedAt())
                .updatedBy(faqLink.getUpdatedBy())
                .build();
    }

    /**
     * FAQ 링크를 추가하거나 수정합니다.
     */
    @Transactional
    public FaqLinkResponseDto upsertFaqLink(FaqLinkRequestDto request, String updatedBy) {
        Optional<FaqLink> faqLinkOpt = faqLinkRepository.findTopByOrderById();
        
        FaqLink faqLink;
        if (faqLinkOpt.isPresent()) {
            // 기존 링크가 있으면 업데이트
            faqLink = faqLinkOpt.get();
            faqLink.updateLink(request.getUrl(), updatedBy);
        } else {
            // 새로운 링크 생성
            faqLink = FaqLink.builder()
                    .id(FAQ_LINK_ID)
                    .url(request.getUrl())
                    .updatedBy(updatedBy)
                    .build();
        }
        
        faqLinkRepository.save(faqLink);
        
        log.info("FAQ 링크 저장 완료: linkId={}, url={}", faqLink.getId(), faqLink.getUrl());
        
        return FaqLinkResponseDto.builder()
                .linkId(faqLink.getId())
                .url(faqLink.getUrl())
                .updatedAt(faqLink.getUpdatedAt())
                .updatedBy(faqLink.getUpdatedBy())
                .build();
    }

    /**
     * FAQ 링크를 삭제합니다.
     */
    @Transactional
    public DeleteLinkResponseDto deleteFaqLink() {
        FaqLink faqLink = faqLinkRepository.findTopByOrderById()
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.FAQ_LINK_NOT_FOUND));
        
        String linkId = faqLink.getId();
        faqLinkRepository.delete(faqLink);
        
        log.info("FAQ 링크 삭제 완료: linkId={}", linkId);
        
        return DeleteLinkResponseDto.builder()
                .linkId(linkId)
                .build();
    }
}

