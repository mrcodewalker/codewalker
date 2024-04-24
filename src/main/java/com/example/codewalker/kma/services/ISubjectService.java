package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Subject;

public interface ISubjectService {
    boolean findBySubjectName(String subjectName);
    Subject createSubject(Subject subject);
    Subject findSubjectByname(String subjectName);
    Subject findBySubjectId(Long id);
}
