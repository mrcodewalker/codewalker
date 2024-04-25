package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.models.Student;
import com.example.codewalker.kma.repositories.ScoreRepository;
import com.example.codewalker.kma.responses.ScoreResponse;
import com.example.codewalker.kma.responses.StudentResponse;
import com.example.codewalker.kma.responses.SubjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService implements IScoreService{
    private final ScoreRepository scoreRepository;

    private final StudentService studentService;

    @Override
    public List<ScoreResponse> getScoreByStudentCode(String studentCode) {
        Long studentId = studentService.findByStudentCode(studentCode).getStudentId();
        List<Score> list = scoreRepository.findByStudentId(studentId);
        List<ScoreResponse> data = new ArrayList<>();
        for (Score clone : list){
            ScoreResponse scoreResponse = ScoreResponse.builder()
                    .scoreFinal(clone.getScoreFinal())
                    .scoreSecond(clone.getScoreSecond())
                    .scoreOverall(clone.getScoreOverall())
                    .scoreFirst(clone.getScoreFirst())
                    .scoreText(clone.getScoreText())
                    .studentResponse(StudentResponse.builder()
                            .studentClass(clone.getStudent().getStudentClass())
                            .studentName(clone.getStudent().getStudentName())
                            .studentCode(clone.getStudent().getStudentCode())
                            .build())
                    .subjectResponse(SubjectResponse.builder()
                            .subjectName(clone.getSubject().getSubjectName())
                            .build())
                    .build();
            data.add(scoreResponse);
        }
        return data;
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
                score.setId(entry.getId());
                score.setScoreFirst(score.getScoreFirst());
                score.setScoreOverall(score.getScoreOverall());
                score.setScoreFinal(score.getScoreFinal());
                score.setScoreText(score.getScoreText());
                score.setScoreSecond(score.getScoreSecond());
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
