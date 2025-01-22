package io.research.surveyservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put("bootstrap.servers", "localhost:9094");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic surveyCreatedTopic() {
        return new NewTopic("survey-created", 1, (short) 1); // topic name, partitions, replication factor
    }

    @Bean
    public NewTopic surveyUpdated() {
        return new NewTopic("survey-updated", 1, (short) 1);
    }

    @Bean
    public NewTopic surveyDeleted() {
        return new NewTopic("survey-deleted", 1, (short) 1);
    }

}
