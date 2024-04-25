package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponse {

    @JsonProperty("student_code")
    private String studentCode;

    @JsonProperty("student_name")
    private String studentName;

    @JsonProperty("student_class")
    private String studentClass;
}
