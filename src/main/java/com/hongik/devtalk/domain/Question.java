package com.hongik.devtalk.domain;

import com.hongik.devtalk.domain.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

//아이디, 학생(1:N), 세션(1:N), 질문내용(content)- 최대80자,
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "question_id")
    private Long id;

    @Column(length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;
}
