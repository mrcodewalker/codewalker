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
public class StudentDTO {
    @JsonProperty("student_id")
    private Long studentId;

    @JsonProperty("student_code")
    private String studentCode;

    @JsonProperty("student_name")
    private String studentName;

    @JsonProperty("student_class")
    private String studentClass;
}
