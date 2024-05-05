package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Ranking;
import com.example.codewalker.kma.models.Student;
import com.example.codewalker.kma.responses.RankingResponse;

import java.util.List;

public interface IRankingService {
    void updateRanking();
    List<RankingResponse> findSchoolRanking(String studentCode);
    void updateGPA();
    RankingResponse findByRanking(Long ranking);
    List<RankingResponse> findBlockRanking(String studentCode);
    List<RankingResponse> findClassRanking(String studentCode);
    List<RankingResponse> findMajorRanking(String studentCode);
    List<RankingResponse> findBlockDetailRanking(String studentCode);
}
