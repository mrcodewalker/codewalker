package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Student;

import java.util.List;

public interface IStudentService {
    Student findByStudentCode(String studentCode);
    Student findById(Long id);
    List<Student> findByName(String studentName);
    List<Student> findByClass(String studentClass);
    Student createStudent(Student student);
}
