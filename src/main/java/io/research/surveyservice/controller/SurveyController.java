package io.research.surveyservice.controller;

import io.research.surveyservice.repository.entity.Option;
import io.research.surveyservice.repository.entity.Question;
import io.research.surveyservice.repository.entity.Survey;
import io.research.surveyservice.service.SurveyNotFoundException;
import io.research.surveyservice.service.SurveyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody CreateSurveyDTO survey) {

        Survey createdSurvey = surveyService.createSurvey(mapToInternalSurvey(survey));

        return new ResponseEntity<>(createdSurvey, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Survey> getSurvey(@RequestParam Long id) {
        try {
            Survey survey = surveyService.getSurveyById(id);
            return new ResponseEntity<>(survey, HttpStatus.OK);
        } catch (SurveyNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Survey> updateSurvey(@PathVariable Long id, @RequestBody Survey survey) {
        Survey updatedSurvey = null;
        try {
            updatedSurvey = surveyService.updateSurvey(id, survey);
            return new ResponseEntity<>(updatedSurvey, HttpStatus.OK);
        } catch (SurveyNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        try {
            surveyService.deleteSurvey(id);
            return ResponseEntity.noContent().build();
        } catch (SurveyNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/pass")
    public ResponseEntity<Void> passSurvey(@RequestBody PassSurveyDTO passSurveyDTO) {
        try {
            surveyService.passSurvey(passSurveyDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (SurveyNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PassSurveyDTO {
        private Long surveyId;
        private List<AnswerDTO> answers;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class AnswerDTO {
            private Long questionId;
            private Long optionId;
            private String answerText;
        }
    }


    private Survey mapToInternalSurvey(CreateSurveyDTO survey) {
        return new Survey(
                null,
                survey.getTitle(),
                survey.getFilmName(),
                survey.getDescription(),
                mapToInternalQuestions(survey.getQuestions())
        );
    }

    private List<Question> mapToInternalQuestions(List<CreateQuestionDTO> questions) {
        return questions.stream()
                .map(question -> new Question(
                        null,
                        question.getQuestionText(),
                        null,
                        null,
                        mapToInternalOptions(question.getOptions())
                ))
                .toList();
    }

    private List<Option> mapToInternalOptions(List<CreateOptionDTO> options) {
        return options.stream()
                .map(option -> new Option(null, option.getOptionText(), null))
                .toList();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateSurveyDTO {
        private String title;
        private String filmName;
        private String description;
        private List<CreateQuestionDTO> questions;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateQuestionDTO {
        private String questionText;
        private List<CreateOptionDTO> options;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CreateOptionDTO {
        private String optionText;
    }
}
