package io.research.surveyservice.publisher;

import io.research.surveyservice.repository.entity.Survey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public EventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishSurveyCreatedEvent(Survey survey) {
        String eventMessage = "Survey Created: " + survey.getTitle();
        kafkaTemplate.send("survey-created", eventMessage);
    }

    public void publishSurveyUpdatedEvent(Survey survey) {
        String eventMessage = "Survey Updated: " + survey.getTitle();
        kafkaTemplate.send("survey-updated", eventMessage);
    }

    public void publishSurveyDeletedEvent(Long surveyId) {
        String eventMessage = "Survey Deleted with ID: " + surveyId;
        kafkaTemplate.send("survey-deleted", eventMessage);
    }
}
