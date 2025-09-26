package com.hongik.devtalk.controller.seminar;

import com.hongik.devtalk.domain.seminar.dto.SeminarDetailResponseDto;
import com.hongik.devtalk.domain.seminar.dto.SeminarDetailReviewResponseDto;
import com.hongik.devtalk.domain.seminar.dto.SeminarDetailSessionResponseDto;
import com.hongik.devtalk.domain.seminar.dto.SeminarDetailSpeakerResponseDto;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name="SeminarDetail",description = "세미나 세부정보 관련 API")
@RestController
@RequestMapping("/user/seminars")
public class SeminarDetailController {

    //세미나 세부정보 조회( 세미나 )

    @Operation(summary = "세미나 세부정보 (세미나) 조회", description = "seminarId를 사용하여 해당 세미나의 정보를 조회합니다.")
    @GetMapping("/{seminarId}")
    public ApiResponse<SeminarDetailResponseDto> getSeminars(@PathVariable Long seminarId)
    {
        SeminarDetailResponseDto test =SeminarDetailResponseDto.builder()
                .seminarId(seminarId)
                .seminarNum(10)
                .topic("LLM을 파헤치다")
                .ImageUrl(".png")
                .seminarDate(LocalDateTime.now())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .place("T동 201호")
                .fileUrl("")
                .build();

        return ApiResponse.onSuccess("세미나 세부정보 ( 세미나 ) 조회에 성공하였습니다. ",test);

    }


    //세미나 세부정보 조회 ( 리뷰 )

    @Operation(summary = "세미나 세부정보 (리뷰) 조회", description = "seminarId를 사용하여 해당 세미나의 리뷰 목록을 조회합니다.")
    @GetMapping("/{seminarId}/review")
    public ApiResponse<List<SeminarDetailReviewResponseDto>> getSeminarReviews(@PathVariable Long seminarId)
    {
        SeminarDetailReviewResponseDto review1 = SeminarDetailReviewResponseDto.builder()
                .reviewId(1L)
                .score((byte) 5)
                .strength("내용이 정말 알찼습니다! 현업에 바로 적용해볼 수 있는 팁들이 많아서 좋았어요.")
                .build();

        SeminarDetailReviewResponseDto review2 = SeminarDetailReviewResponseDto.builder()
                .reviewId(2L)
                .score((byte) 4)
                .strength("두 번째 세션이 조금 어려웠지만, 전반적으로 만족스러운 세미나였습니다.")
                .build();

        List<SeminarDetailReviewResponseDto> reviewList = List.of(review1, review2);

        return ApiResponse.onSuccess("세미나 세부정보 (리뷰) 조회에 성공하였습니다.", reviewList);

    }


    //세미나 세부정보 조회 ( 세션 )

    @Operation(summary = "세미나 세부정보 (세션) 조회", description = "seminarId를 사용하여 해당 세미나의 세션 목록을 조회합니다.")
    @GetMapping("/{seminarId}/session")
    public ApiResponse<List<SeminarDetailSessionResponseDto>> getSeminarSessions(@PathVariable Long seminarId)
    {
        SeminarDetailSpeakerResponseDto speaker1 = SeminarDetailSpeakerResponseDto.builder()
                .SpeakerId(1L)
                .name("김연사")
                .organization("데브톡 테크")
                .history("전 ) 카카오 ")
                .build();

        SeminarDetailSpeakerResponseDto speaker2 = SeminarDetailSpeakerResponseDto.builder()
                .SpeakerId(2L)
                .name("박연사")
                .organization("데브톡 테크2")
                .history("전 ) 토스 ")
                .build();


        SeminarDetailSessionResponseDto session1 = SeminarDetailSessionResponseDto.builder()
                .sessionId(1L)
                .title("Data Scientist가 바라보는 AI의 지난 10년과 현재")
                .description("LLM은 어쩌다 이렇게 똑똑해졌을까요? AI의 발전사를 데이터와 함께 알아봅니다.")
                .speaker(speaker1) // .speaker() 메소드에 생성된 speaker1 객체를 전달
                .build();

        SeminarDetailSessionResponseDto session2 = SeminarDetailSessionResponseDto.builder()
                .sessionId(2L)
                .title("Data Scientist가 바라보는 AI의 지난 10년과 현재")
                .description("Virtual Thread, AOT 컴파일 등 Spring Boot 3.x의 새로운 기능들을 알아보고 실제 MSA 환경에 적용하는 방법을 논의합니다.")
                .speaker(speaker2) // .speaker() 메소드에 생성된 speaker2 객체를 전달
                .build();


        List<SeminarDetailSessionResponseDto> sessionList = List.of(session1, session2);

        return ApiResponse.onSuccess("세미나 세션 목록 조회에 성공하였습니다.", sessionList);

    }


}
