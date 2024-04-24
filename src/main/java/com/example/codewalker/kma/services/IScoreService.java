package com.example.codewalker.kma.services;

import com.example.codewalker.kma.models.Score;

import java.util.List;

public interface IScoreService {
    List<Score> getScoreByStudentCode(String studentCode);
    Score createScore(Score score);
    List<Score> findAll();
}
