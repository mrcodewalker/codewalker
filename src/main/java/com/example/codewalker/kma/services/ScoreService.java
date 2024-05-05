package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.models.Student;
import com.example.codewalker.kma.repositories.ScoreRepository;
import com.example.codewalker.kma.repositories.StudentRepository;
import com.example.codewalker.kma.responses.ListScoreResponse;
import com.example.codewalker.kma.responses.ScoreResponse;
import com.example.codewalker.kma.responses.StudentResponse;
import com.example.codewalker.kma.responses.SubjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreService implements IScoreService{
    private final ScoreRepository scoreRepository;
    private final StudentRepository studentRepository;

    private final StudentService studentService;

    @Override
    public ListScoreResponse getScoreByStudentCode(String studentCode) {
        Long studentId = studentService.findByStudentCode(studentCode).getStudentId();
        Student student = this.studentService.findByStudentCode(studentCode);
        List<Score> list = scoreRepository.findByStudentId(studentId);
        List<ScoreResponse> data = new ArrayList<>();
        for (Score clone : list){
            ScoreResponse scoreResponse = ScoreResponse.builder()
                    .scoreFinal(clone.getScoreFinal())
                    .scoreSecond(clone.getScoreSecond())
                    .scoreOverall(clone.getScoreOverall())
                    .scoreFirst(clone.getScoreFirst())
                    .scoreText(clone.getScoreText())
                    .subjectName(clone.getSubject().getSubjectName())
                    .build();
            data.add(scoreResponse);
        }
        return ListScoreResponse.builder()
                .studentResponse(StudentResponse.builder()
                        .studentClass(student.getStudentClass())
                        .studentName(student.getStudentName())
                        .studentCode(student.getStudentCode())
                        .build())
                .scoreResponses(data)
                .build();
    }
    public List<Score> findByStudentCode(String studentCode){
        Student student = studentService.findByStudentCode(studentCode);
        return scoreRepository.findByStudent(student);
    }
    @Override
    public Score createScore(Score score) {
        List<Score> data = scoreRepository.findByStudentCode(score.getStudent().getStudentCode());
        for (Score entry : data){
            if (entry.getSubject().getSubjectName().equals(score.getSubject().getSubjectName())
        || Normalizer.normalize(entry.getSubject().getSubjectName(), Normalizer.Form.NFD).replaceAll("\\p{M}", "").equalsIgnoreCase(
                    Normalizer.normalize(score.getSubject().getSubjectName(), Normalizer.Form.NFD).replaceAll("\\p{M}", ""))){
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

    @Override
    public void updateRanking() {

    }

}
