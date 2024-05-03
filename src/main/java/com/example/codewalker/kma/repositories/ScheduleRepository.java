package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Schedule;
import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    @Query("SELECT s FROM Schedule s WHERE s.subjectName LIKE %:keyword%")
    // AND s.startDay >= CURRENT_DATE
    List<Schedule> findBySubjectNameContainingKeyword(@Param("keyword") String keyword);
}
