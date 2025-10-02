package com.hongik.devtalk.domain;

import com.hongik.devtalk.domain.common.BaseTimeEntity;
import com.hongik.devtalk.domain.enums.Department;
import com.hongik.devtalk.domain.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Student extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String studentNum;

    @Column(length = 50, nullable = false)
    private String name;

    //기본 선택 (1,2,3,4학년인 경우)
    private Integer grade;

    //기타 선택 (학생 직접 입력)
    private String gradeEtc;

    @Column(unique = true)
    private String email;

    @Column(length = 20, unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentStatus status = StudentStatus.ACTIVE;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "student_departments",
            joinColumns = @JoinColumn(name = "student_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "department_name")
    private Set<Department> departments = new HashSet<>();

    private String departmentEtc;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Applicant> applicants = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Inquiry> inquiries = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    public void updateDetails(String name, String phone, Integer grade, String gradeEtc, Set<Department> departments, String departmentEtc) {
        this.name = name;
        this.phone = phone;
        this.grade = grade;
        this.gradeEtc = gradeEtc;
        this.departments = departments;
        this.departmentEtc = departmentEtc;
    }

}
