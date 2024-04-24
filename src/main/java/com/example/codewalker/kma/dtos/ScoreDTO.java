package com.example.codewalker.kma.dtos;

import com.example.codewalker.kma.models.Student;
import com.example.codewalker.kma.models.Subject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
public class ScoreDTO {
    @JsonProperty("student_id")
    private Long studentId;

    @JsonProperty("subject_id")
    private Long subjectId;

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
