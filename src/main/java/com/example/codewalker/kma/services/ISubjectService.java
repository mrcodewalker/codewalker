package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Subject;

import java.util.List;

public interface ISubjectService {
//    boolean findBySubjectName(String subjectName);
    Subject createSubject(Subject subject);
    Subject findSubjectByName(String subjectName);
    Subject findBySubjectId(Long id);
    boolean existBySubjectName(String subjectName);
    List<Subject> findAll();
}
