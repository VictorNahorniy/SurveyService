package io.research.surveyservice.service;

import io.research.surveyservice.controller.SurveyController;
import io.research.surveyservice.publisher.EventPublisher;
import io.research.surveyservice.repository.AnswerRepository;
import io.research.surveyservice.repository.SurveyRepository;
import io.research.surveyservice.repository.entity.Answer;
import io.research.surveyservice.repository.entity.Option;
import io.research.surveyservice.repository.entity.Question;
import io.research.surveyservice.repository.entity.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final AnswerRepository answerRepository;
    private final EventPublisher eventPublisher;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository, AnswerRepository answerRepository, EventPublisher eventPublisher) {
        this.surveyRepository = surveyRepository;
        this.answerRepository = answerRepository;
        this.eventPublisher = eventPublisher;
    }

    public Survey getSurveyById(Long id) throws SurveyNotFoundException {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new SurveyNotFoundException(id));
    }

    public Survey createSurvey(Survey survey) {
        Survey savedSurvey = surveyRepository.save(survey);
        eventPublisher.publishSurveyCreatedEvent(savedSurvey);
        return savedSurvey;
    }

    public Survey updateSurvey(Long id, Survey survey) throws SurveyNotFoundException {
        if (!surveyRepository.existsById(id)) {
            throw new SurveyNotFoundException(id);
        }
        survey.setId(id);
        Survey updatedSurvey = surveyRepository.save(survey);
        eventPublisher.publishSurveyUpdatedEvent(updatedSurvey);
        return updatedSurvey;
    }

    public void deleteSurvey(Long id) throws SurveyNotFoundException {
        if (!surveyRepository.existsById(id)) {
            throw new SurveyNotFoundException(id);
        }
        surveyRepository.deleteById(id);
        eventPublisher.publishSurveyDeletedEvent(id);
    }

    public void passSurvey(SurveyController.PassSurveyDTO passSurveyDTO) throws SurveyNotFoundException {
        // Find the survey by ID
        Survey survey = surveyRepository.findById(passSurveyDTO.getSurveyId())
                .orElseThrow(() -> new SurveyNotFoundException(passSurveyDTO.getSurveyId()));

        // Process each answer
        List<Answer> answers = passSurveyDTO.getAnswers().stream()
                .map(answerDTO -> {
                    Question question = findQuestionById(survey, answerDTO.getQuestionId());
                    Option option = findOptionById(question, answerDTO.getOptionId());

                    // Create and associate the answer with the question
                    return new Answer(null, option.getOptionText(), question);
                })
                .collect(Collectors.toList());

        // Save the answers in the database
        answerRepository.saveAll(answers);
    }

    private Question findQuestionById(Survey survey, Long questionId) {
        return survey.getQuestions().stream()
                .filter(question -> question.getId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));
    }

    private Option findOptionById(Question question, Long optionId) {
        return question.getOptions().stream()
                .filter(option -> option.getId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Option not found"));
    }

}
