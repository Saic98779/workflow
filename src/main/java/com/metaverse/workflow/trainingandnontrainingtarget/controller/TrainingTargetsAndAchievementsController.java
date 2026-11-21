package com.metaverse.workflow.trainingandnontrainingtarget.controller;

import com.metaverse.workflow.pdfandexcelgenerater.service.TrainingAndNonTrainingTargetsExcelGenerator;
import com.metaverse.workflow.pdfandexcelgenerater.service.TrainingTargetsAndAchievementsExcelGenerator;
import com.metaverse.workflow.trainingandnontrainingtarget.dtos.TrainingTargetsAndAchievementsResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TargetResponse;
import com.metaverse.workflow.trainingandnontrainingtarget.service.TrainingTargetsAndAchievementsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/training")
@RequiredArgsConstructor
public class TrainingTargetsAndAchievementsController {

    private static final MediaType XLSX_MEDIA_TYPE =
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final TrainingTargetsAndAchievementsService trainingAndAchievementsService;
    private final TrainingTargetsAndAchievementsExcelGenerator trainingTargetsAndAchievementsExcelGenerator;
    private final TrainingAndNonTrainingTargetsExcelGenerator trainingAndNonTrainingTargetsExcelGenerator;

    @GetMapping("/targets-and-achievements/agency/{agencyId}")
    public ResponseEntity<?> getAllTrainingTargets(@RequestParam String year, @PathVariable("agencyId") Long agencyId) {
        List<TrainingTargetsAndAchievementsResponse> response = trainingAndAchievementsService.getTargetsAndAchievements(year, agencyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/targets/by-agency")
    public ResponseEntity<?> getAllTrainingTargetsByAgencyId(@RequestParam(required = false) String year,
                                                             @RequestParam(required = false) Long agencyId) {
        List<TargetResponse> response = trainingAndAchievementsService.getTrainingTargets(year, agencyId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/targets-and-achievements/export/excel")
    public ResponseEntity<byte[]> exportTargetsAndAchievementsExcel(@RequestParam(required = false) String year,
                                                                    @RequestParam(required = false) Long agencyId) throws IOException {
        byte[] excelBytes = trainingTargetsAndAchievementsExcelGenerator.generateExcel(year, agencyId);

        StringBuilder fileName = new StringBuilder("Training_Targets_And_Achievements");
        if (agencyId != null) {
            fileName.append("_Agency_").append(agencyId);
        }
        if (year != null && !year.isBlank()) {
            fileName.append("_").append(year.replaceAll("[^0-9-]", ""));
        }
        fileName.append(".xlsx");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(XLSX_MEDIA_TYPE)
                .contentLength(excelBytes.length)
                .body(excelBytes);
    }

    @GetMapping("/targets/training-and-non-training/export/excel")
    public ResponseEntity<byte[]> exportTrainingAndNonTrainingTargetsExcel(@RequestParam(required = false) String year,
                                                                           @RequestParam(required = false) Long agencyId) throws IOException {
        byte[] excelBytes = trainingAndNonTrainingTargetsExcelGenerator.generateExcel(year, agencyId);

        StringBuilder fileName = new StringBuilder("Training_And_NonTraining_Targets");
        if (agencyId != null) {
            fileName.append("_Agency_").append(agencyId);
        }
        if (year != null && !year.isBlank()) {
            fileName.append("_").append(year.replaceAll("[^0-9-]", ""));
        }
        fileName.append(".xlsx");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(XLSX_MEDIA_TYPE)
                .contentLength(excelBytes.length)
                .body(excelBytes);
    }
}
