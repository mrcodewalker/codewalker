package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScoreResponse {

    @JsonProperty("subject_name")
    private String subjectName;

    @JsonProperty("score_text")
    private String scoreText;

    @JsonProperty("score_first")
    private Float scoreFirst;

    @JsonProperty("score_second")
    private Float scoreSecond;

    @JsonProperty("score_final")
    private Float scoreFinal;

    @JsonProperty("score_over_rall")
    private Float scoreOverall;
}
