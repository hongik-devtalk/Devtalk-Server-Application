package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String departmentName;

    @OneToMany(mappedBy = "department")
    private List<StudentDepartmentId> studentDepartments = new ArrayList<>();
}
