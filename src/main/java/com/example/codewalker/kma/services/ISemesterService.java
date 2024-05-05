package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.models.Semester;
import com.example.codewalker.kma.responses.ListScoreResponse;
import com.example.codewalker.kma.responses.RankingResponse;

import java.util.List;

public interface ISemesterService {
//    ListScoreResponse getScoreByStudentCode(String studentCode);
    Semester createSemester(Semester semester);
    List<Semester> findAll();
    void updateRanking();
    void deleteAll();
    List<RankingResponse> getScholarship(String studentCode);
}
