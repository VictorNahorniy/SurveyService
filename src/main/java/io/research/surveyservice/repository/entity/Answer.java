package io.research.surveyservice.repository.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "answerEntity")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
