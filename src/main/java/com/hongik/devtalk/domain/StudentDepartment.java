package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@IdClass(StudentDepartmentId.class)
public class StudentDepartment {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

}
