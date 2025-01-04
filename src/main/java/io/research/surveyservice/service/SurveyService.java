package io.research.surveyservice.service;

import io.research.surveyservice.publisher.EventPublisher;
import io.research.surveyservice.repository.SurveyRepository;
import io.research.surveyservice.repository.entity.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final EventPublisher eventPublisher;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository, EventPublisher eventPublisher) {
        this.surveyRepository = surveyRepository;
        this.eventPublisher = eventPublisher;
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
}
