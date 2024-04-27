package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListScoreResponse {
    @JsonProperty("student_response")
    private StudentResponse studentResponse;
    @JsonProperty("scores_response")
    private List<ScoreResponse> scoreResponses;
}
