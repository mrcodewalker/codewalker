package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Ranking;
import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.models.Semester;
import com.example.codewalker.kma.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SemesterRepository extends JpaRepository<Semester,Long> {
    List<Semester> findByStudent(Student student);
    @Query("SELECT s FROM Semester s WHERE s.student.studentCode = :studentCode")
    List<Semester> findByStudentCode(@Param("studentCode") String studentCode);
    @Query("SELECT s FROM Semester s WHERE s.student.studentId = :studentId")
    List<Semester> findByStudentId(@Param("studentId") Long studentId);
}
