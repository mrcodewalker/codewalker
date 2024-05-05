package com.example.codewalker.kma.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponse {
    @JsonProperty("subject_credits")
    private Long subjectCredits;
    @JsonProperty("subject_name")
    private String subjectName;
    @JsonProperty("day_in_week")
    private Long dayInWeek;
    @JsonProperty("lesson_number")
    private String lessonNumber;
    @JsonProperty("start_day")
    private Date startDay;
    @JsonProperty("end_day")
    private Date endDay;
}
