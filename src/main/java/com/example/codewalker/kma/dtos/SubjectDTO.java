package com.example.codewalker.kma.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
@Builder
public class SubjectDTO {
    @JsonProperty("subject_id")
    private Long id;

    @JsonProperty("subject_name")
    private String subjectName;
}
