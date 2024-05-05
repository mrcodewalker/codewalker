package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.*;
import com.example.codewalker.kma.repositories.SemesterRepository;
import com.example.codewalker.kma.repositories.StudentRepository;
import com.example.codewalker.kma.responses.RankingResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SemesterService implements ISemesterService{
    private final SemesterRepository semesterRepository;
    private final EntityManager entityManager;
    private final StudentRepository studentRepository;

    @Override
    public Semester createSemester(Semester semester) {
        List<Semester> data = semesterRepository.findByStudentCode(semester.getStudent().getStudentCode());
        for (Semester entry : data){
            if (entry.getSubject().getSubjectName().equals(semester.getSubject().getSubjectName())
                    || Normalizer.normalize(entry.getSubject().getSubjectName(), Normalizer.Form.NFD).replaceAll("\\p{M}", "").equalsIgnoreCase(
                    Normalizer.normalize(semester.getSubject().getSubjectName(), Normalizer.Form.NFD).replaceAll("\\p{M}", ""))){
                semester.setId(entry.getId());
                semester.setScoreFirst(semester.getScoreFirst());
                semester.setScoreOverall(semester.getScoreOverall());
                semester.setScoreFinal(semester.getScoreFinal());
                semester.setScoreText(semester.getScoreText());
                semester.setScoreSecond(semester.getScoreSecond());
                return semesterRepository.save(semester);
            }
        }
        return semesterRepository.save(semester);
    }

    @Override
    public List<Semester> findAll() {
        return null;
    }

    @Override
    public void updateRanking() {

    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM Semester").executeUpdate();
        entityManager.createQuery("ALTER TABLE Semester AUTO_INCREMENT = 1").executeUpdate();
    }

    @Override
    public List<RankingResponse> getScholarship(String studentCode) {
        return null;
    }
}
