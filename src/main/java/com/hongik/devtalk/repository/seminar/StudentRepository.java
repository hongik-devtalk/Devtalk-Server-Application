package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Optional<Student> findByStudentNumAndName(String studentNum, String name);

    Optional<Student> findByStudentNum(String studentNum);
}
