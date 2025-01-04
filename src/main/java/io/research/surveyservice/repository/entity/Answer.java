package io.research.surveyservice.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "answerEntity")
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String answerText;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    // Конструктори, геттери, сеттери
}
