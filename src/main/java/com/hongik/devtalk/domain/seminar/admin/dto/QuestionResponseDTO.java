package com.hongik.devtalk.domain.seminar.admin.dto;

import com.hongik.devtalk.domain.Question;
import com.hongik.devtalk.domain.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponseDTO {

    private int seminarNum;
    private List<SpeakerDTO> speakers;
    private List<StudentQuestionDTO> students;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SpeakerDTO {
        private Long speakerId;
        private String speakerName;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StudentQuestionDTO {
        private Long studentId;
        private String studentNum;
        private String studentName;
        private String phoneNum;
        private List<QuestionDTO> questions;

        public static StudentQuestionDTO from(Student student, List<Question> questions) {
            return StudentQuestionDTO.builder()
                    .studentId(student.getId())
                    .studentNum(student.getStudentNum())
                    .studentName(student.getName())
                    .phoneNum(student.getPhone())
                    .questions(
                            questions.stream()
                                    .map(q -> QuestionDTO.builder()
                                            .speakerId(q.getSession().getSpeaker().getId())
                                            .content(q.getContent())
                                            .build())
                                    .toList()
                    )
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class QuestionDTO {
        private Long speakerId;
        private String content;
    }
}
