package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject,Long> {
    Subject findBySubjectName(String subjectName);
}
