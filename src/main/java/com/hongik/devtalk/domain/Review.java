package com.hongik.devtalk.domain;

import com.hongik.devtalk.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // 세미나별로 후기 작성
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id", nullable = false)
    private Seminar seminar;

    //좋았던 점
    @Lob
    @Column(nullable = false)
    private String strength;

    //아쉬웠던 점
    @Lob
    private String improvement;

    //다음에 듣고 싶은 주제
    @Lob
    private String nextTopic;

    //별점
    @Column(nullable = false)
    private byte score;

    //공개 여부
    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean isPublic;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isNote;

    // 홈화면 노출 여부 업데이트
    public void updateIsNote(boolean isNote) {
        this.isNote = isNote;
    }
}
