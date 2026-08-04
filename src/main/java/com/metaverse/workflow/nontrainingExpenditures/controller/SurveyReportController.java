package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.SurveyReportDTO;
import com.metaverse.workflow.nontrainingExpenditures.service.SurveyReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/survey-report")
@RequiredArgsConstructor
public class SurveyReportController {

    private final SurveyReportService surveyReportService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public WorkflowResponse create(
            @RequestPart("surveyReport") String surveyReport,
            @RequestPart(value = "file", required = false) MultipartFile file)
            throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        SurveyReportDTO dto = objectMapper.readValue(surveyReport, SurveyReportDTO.class);

        return surveyReportService.create(dto, file);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SurveyReportDTO update(
            @PathVariable Long id,
            @RequestPart("surveyReport") String surveyReport,
            @RequestPart(value = "file", required = false) MultipartFile file)
            throws DataException, JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        SurveyReportDTO dto = objectMapper.readValue(surveyReport, SurveyReportDTO.class);

        return surveyReportService.update(id, dto, file);
    }

    @GetMapping("/get-all")
    public List<SurveyReportDTO> getAll() {
        return surveyReportService.getAll();
    }

    @GetMapping("/get/{id}")
    public SurveyReportDTO getById(@PathVariable Long id)
            throws DataException {

        return surveyReportService.getById(id);
    }

    @GetMapping("/sub-activity/{subActivityId}")
    public WorkflowResponse getBySubActivityId(@PathVariable Long subActivityId)
            throws DataException {

        return surveyReportService.getBySubActivityId(subActivityId);
    }

    @DeleteMapping("/delete/{id}")
    public WorkflowResponse delete(@PathVariable Long id)
            throws DataException {

        surveyReportService.delete(id);

        return WorkflowResponse.builder()
                .status(200)
                .message("Survey Report deleted successfully")
                .build();
    }
}
