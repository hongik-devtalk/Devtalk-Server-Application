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

    @Column(length = 100)
    private String nickname;

    @Lob
    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private byte score;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isPublic;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isNote;
}
