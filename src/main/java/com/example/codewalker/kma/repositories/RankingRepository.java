package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Ranking;
import com.example.codewalker.kma.models.Schedule;
import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RankingRepository extends JpaRepository<Ranking,Long> {
    @Query("SELECT r FROM Ranking r WHERE r.student.studentId = :studentId")
    Ranking findByStudentId(@Param("studentId") Long studentId);
    Ranking findByRanking(Long ranking);
}
