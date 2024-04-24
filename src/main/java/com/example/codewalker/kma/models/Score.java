package com.example.codewalker.kma.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "scores")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", referencedColumnName = "subject_id", nullable = false)
    private Subject subject;

    @Column(name = "score_text", length = 100, nullable = false, columnDefinition = "VARCHAR(100) DEFAULT ''")
    private String scoreText;

    @Column(name = "score_first", nullable = false)
    private Float scoreFirst;

    @Column(name = "score_second", nullable = false)
    private Float scoreSecond;

    @Column(name = "score_final", nullable = false)
    private Float scoreFinal;

    @Column(name = "score_over_rall", nullable = false)
    private Float scoreOverall;
}

