package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Subject;
import com.example.codewalker.kma.repositories.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService implements ISubjectService {
    private final SubjectRepository subjectRepository;

    @Override
    public boolean findBySubjectName(String subjectName) {
        if (subjectRepository.findBySubjectName(subjectName).getSubjectName().length()>=1){
            return true;
        }
        return false;
    }

    @Override
    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    @Override
    public Subject findSubjectByname(String subjectName) {
        return subjectRepository.findBySubjectName(subjectName);
    }

    @Override
    public Subject findBySubjectId(Long id) {
        Optional<Subject> subject = subjectRepository.findById(id);
        if (subject.isPresent()){
            return subject.get();
        }
        return null;
    }
}
