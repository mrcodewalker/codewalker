package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Student;
import com.example.codewalker.kma.repositories.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService{
    private final StudentRepository studentRepository;
    @Override
    public Student findByStudentCode(String studentCode) {
        return studentRepository.findByStudentCode(studentCode);
    }

    @Override
    public Student findById(Long id) {
        if(studentRepository.findById(id).isPresent()){
            return studentRepository.findById(id).get();
        } else {
            return null;
        }
    }

    @Override
    public List<Student> findByName(String studentName) {
        return studentRepository.findByStudentName(studentName);
    }

    @Override
    public List<Student> findByClass(String studentClass) {
        return studentRepository.findByStudentClass(studentClass);
    }

    @Override
    public Student createStudent(Student student) {
        Student clone = studentRepository.findByStudentCode(student.getStudentCode());
        if (clone==null) {
            return studentRepository.save(student);
        }
        return null;
    }

    @Override
    public boolean existByStudentCode(String studentCode) {
        return studentRepository.existByStudentCode(studentCode);
    }
}
