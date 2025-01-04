package io.research.surveyservice.controller;

import io.research.surveyservice.repository.entity.Survey;
import io.research.surveyservice.service.SurveyNotFoundException;
import io.research.surveyservice.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    @Autowired
    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody Survey survey) {
        Survey createdSurvey = surveyService.createSurvey(survey);
        return new ResponseEntity<>(createdSurvey, HttpStatus.CREATED);
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
}
