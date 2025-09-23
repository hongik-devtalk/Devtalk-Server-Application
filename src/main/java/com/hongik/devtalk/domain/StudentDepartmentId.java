package com.hongik.devtalk.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@EqualsAndHashCode
public class StudentDepartmentId implements Serializable {
    private Long student;
    private Long department;
}
