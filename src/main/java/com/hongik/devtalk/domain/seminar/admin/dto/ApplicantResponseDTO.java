package com.hongik.devtalk.domain.seminar.admin.dto;

import com.hongik.devtalk.domain.enums.InflowPath;
import com.hongik.devtalk.domain.enums.ParticipationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicantResponseDTO {
    private String topic;
    private String studentId;
    private String studentNum;
    private String department;
    private String grade;
    private String name;
    private String phone;
    private ParticipationType participationType;
    private InflowPath inflowPath;
}