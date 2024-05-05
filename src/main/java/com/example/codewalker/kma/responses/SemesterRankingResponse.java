package com.example.codewalker.kma.responses;

import com.example.codewalker.kma.models.Ranking;
import com.example.codewalker.kma.models.SemesterRanking;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SemesterRankingResponse {
    @JsonProperty("student_name")
    private String studentName;
    @JsonProperty("student_code")
    private String studentCode;
    @JsonProperty("student_class")
    private String studentClass;
    @JsonProperty("ranking")
    private Long ranking;
    @JsonProperty("gpa")
    private Float gpa;
    @JsonProperty("asia_gpa")
    private Float asiaGpa;
    public SemesterRankingResponse formData(SemesterRanking ranking){
        return SemesterRankingResponse.builder()
                .gpa(ranking.getGpa())
                .studentName(ranking.getStudent().getStudentName())
                .ranking(ranking.getRanking())
                .studentCode(ranking.getStudent().getStudentCode())
                .asiaGpa(ranking.getAsiaGpa())
                .studentClass(ranking.getStudent().getStudentClass())
                .build();
    }
}
