package io.research.surveyservice.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventListener {

    @KafkaListener(topics = "survey-updated", groupId = "survey-service")
    public void listenSurveyUpdated(String message) {
        // Логіка для обробки події оновлення опитування
        System.out.println("Received survey updated event: " + message);
    }
}
