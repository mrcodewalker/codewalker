package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score,Long> {
    List<Score> findByStudent(Student student);
    @Query("SELECT s FROM Score s WHERE s.student.studentCode = :studentCode")
    List<Score> findByStudentCode(@Param("studentCode") String studentCode);
    @Query("SELECT s FROM Score s WHERE s.student.studentId = :studentId")
    List<Score> findByStudentId(@Param("studentId") Long studentId);
}
