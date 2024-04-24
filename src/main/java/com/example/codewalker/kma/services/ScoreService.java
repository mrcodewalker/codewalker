package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.models.Student;
import com.example.codewalker.kma.repositories.ScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService implements IScoreService{
    private final ScoreRepository scoreRepository;

    private final StudentService studentService;

    @Override
    public List<Score> getScoreByStudentCode(String studentCode) {
        Long studentId = studentService.findByStudentCode(studentCode).getStudentId();
        return scoreRepository.findByStudentId(studentId);
    }
    public List<Score> findByStudentCode(String studentCode){
        Student student = studentService.findByStudentCode(studentCode);
        return scoreRepository.findByStudent(student);
    }
    @Override
    public Score createScore(Score score) {
        List<Score> data = scoreRepository.findByStudentCode(score.getStudent().getStudentCode());
        for (Score entry : data){
            if (entry.getSubject().equals(score.getSubject())){
                scoreRepository.delete(entry);
            return scoreRepository.save(score);
            }
        }
        return scoreRepository.save(score);
    }

    @Override
    public List<Score> findAll() {
        return scoreRepository.findAll();
    }
}
