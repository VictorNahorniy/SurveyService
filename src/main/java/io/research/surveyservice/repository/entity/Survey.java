package io.research.surveyservice.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "surveyEntity")
@Getter
@Setter
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<Question> questions;

    // Конструктори, геттери, сеттери
}

