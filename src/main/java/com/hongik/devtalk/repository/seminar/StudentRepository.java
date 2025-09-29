package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findByStudentNum(String studentNum);
}
