package com.example.codewalker.kma.services;

import com.example.codewalker.kma.responses.RankingResponse;
import com.example.codewalker.kma.responses.SemesterRankingResponse;

import java.util.List;

public interface ISemesterRankingService {
    void updateRanking();
    void updateGPA();
    List<SemesterRankingResponse> findRanking(String studentCode);
}
