package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Ranking;
import com.example.codewalker.kma.models.SemesterRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SemesterRankingRepository extends JpaRepository<SemesterRanking,Long> {
    @Query("SELECT s FROM SemesterRanking s WHERE s.student.studentId = :studentId")
    Ranking findByStudentId(@Param("studentId") Long studentId);
    Ranking findByRanking(Long ranking);
}
