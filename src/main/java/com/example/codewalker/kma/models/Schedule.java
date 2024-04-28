package com.example.codewalker.kma.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "subject_credits")
    @JsonProperty("subject_credits")
    private Long subjectCredits;
    @Column(name = "subject_name", nullable = false)
    @JsonProperty("subject_name")
    private String subjectName;

    @Column(name = "day_in_week", nullable = false)
    @JsonProperty("day_in_week")
    private Integer dayInWeek;

    @Column(name = "lesson_number")
    @JsonProperty("lesson_number")
    private String lessonNumber;

    @Column(name = "start_day")
    @JsonProperty("start_day")
    private Date startDay;

    @Column(name = "end_day")
    @JsonProperty("end_day")
    private Date endDay;
}
