package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    Subject findBySubjectName(String subjectName);
    @Query("SELECT COUNT(s) > 0 FROM Subject s WHERE s.subjectName = :subjectName")
    boolean existBySubjectName(String subjectName);
}
