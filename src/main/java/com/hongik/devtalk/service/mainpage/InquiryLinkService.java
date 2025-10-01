package com.hongik.devtalk.service.mainpage;

import com.hongik.devtalk.domain.InquiryLink;
import com.hongik.devtalk.domain.mainpage.dto.InquiryLinkResponseDto;
import com.hongik.devtalk.domain.mainpage.dto.InquiryLinkRequestDto;
import com.hongik.devtalk.domain.mainpage.dto.DeleteLinkResponseDto;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.mainpage.InquiryLinkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryLinkService {

    private final InquiryLinkRepository inquiryLinkRepository;
    
    private static final String INQUIRY_LINK_ID = "inquiry_link_1"; // 고정 ID

    /**
     * 문의하기 링크를 조회합니다.
     */
    public InquiryLinkResponseDto getInquiryLink() {
        Optional<InquiryLink> inquiryLinkOpt = inquiryLinkRepository.findTopByOrderById();
        
        if (inquiryLinkOpt.isEmpty()) {
            // 데이터가 없으면 기본값 반환
            return InquiryLinkResponseDto.builder()
                    .linkId(null)
                    .url(null)
                    .updatedAt(null)
                    .updatedBy(null)
                    .build();
        }

        InquiryLink inquiryLink = inquiryLinkOpt.get();
        
        return InquiryLinkResponseDto.builder()
                .linkId(inquiryLink.getId())
                .url(inquiryLink.getUrl())
                .updatedAt(inquiryLink.getUpdatedAt())
                .updatedBy(inquiryLink.getUpdatedBy())
                .build();
    }

    /**
     * 문의하기 링크를 추가하거나 수정합니다.
     */
    @Transactional
    public InquiryLinkResponseDto upsertInquiryLink(InquiryLinkRequestDto request, String updatedBy) {
        Optional<InquiryLink> inquiryLinkOpt = inquiryLinkRepository.findTopByOrderById();
        
        InquiryLink inquiryLink;
        if (inquiryLinkOpt.isPresent()) {
            // 기존 링크가 있으면 업데이트
            inquiryLink = inquiryLinkOpt.get();
            inquiryLink.updateLink(request.getUrl(), updatedBy);
        } else {
            // 새로운 링크 생성
            inquiryLink = InquiryLink.builder()
                    .id(INQUIRY_LINK_ID)
                    .url(request.getUrl())
                    .updatedBy(updatedBy)
                    .build();
        }
        
        inquiryLinkRepository.save(inquiryLink);
        
        log.info("문의하기 링크 저장 완료: linkId={}, url={}", inquiryLink.getId(), inquiryLink.getUrl());
        
        return InquiryLinkResponseDto.builder()
                .linkId(inquiryLink.getId())
                .url(inquiryLink.getUrl())
                .updatedAt(inquiryLink.getUpdatedAt())
                .updatedBy(inquiryLink.getUpdatedBy())
                .build();
    }

    /**
     * 문의하기 링크를 삭제합니다.
     */
    @Transactional
    public DeleteLinkResponseDto deleteInquiryLink() {
        InquiryLink inquiryLink = inquiryLinkRepository.findTopByOrderById()
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.INQUIRY_LINK_NOT_FOUND));
        
        String linkId = inquiryLink.getId();
        inquiryLinkRepository.delete(inquiryLink);
        
        log.info("문의하기 링크 삭제 완료: linkId={}", linkId);
        
        return DeleteLinkResponseDto.builder()
                .linkId(linkId)
                .build();
    }
}
