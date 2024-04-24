package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findByStudentCode(String studentCode);
    List<Student> findByStudentName(String studentName);
    List<Student> findByStudentClass(String studentClass);
}
