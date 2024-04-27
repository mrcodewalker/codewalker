package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Score;
import com.example.codewalker.kma.responses.ListScoreResponse;
import com.example.codewalker.kma.responses.ScoreResponse;

import java.util.List;

public interface IScoreService {
    ListScoreResponse getScoreByStudentCode(String studentCode);
    Score createScore(Score score);
    List<Score> findAll();
}
