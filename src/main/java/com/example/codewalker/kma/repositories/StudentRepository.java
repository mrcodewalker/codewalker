package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Student findByStudentCode(String studentCode);
    List<Student> findByStudentName(String studentName);
    List<Student> findByStudentClass(String studentClass);
    @Query("SELECT COUNT(s) > 0 FROM Student s WHERE s.studentCode = :studentCode")
    boolean existByStudentCode(String studentCode);
}
