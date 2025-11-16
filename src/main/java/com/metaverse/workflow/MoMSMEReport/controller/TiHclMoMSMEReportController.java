package com.metaverse.workflow.MoMSMEReport.controller;

import com.metaverse.workflow.MoMSMEReport.service.MoMSMERowDTO;
import com.metaverse.workflow.MoMSMEReport.service.TiHclMoMSMEReportService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

import java.util.Map;
@RestController
@RequiredArgsConstructor
public class TiHclMoMSMEReportController {
    private final TiHclMoMSMEReportService tiHclMoMSMEReportService;
    @GetMapping(value = "/tihcl-momsme-report-dto", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> corpusDebitFinancing() {
        try {
            return ResponseEntity.ok(
                    WorkflowResponse.builder()
                            .status(200)
                            .message("fetched")
                            .data(tiHclMoMSMEReportService.fetchMoMsmeReport())
                            .build()
            );
        } catch (RestClientException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }





        @GetMapping("/rows/{subActivityId}")
        public ResponseEntity<?> getMoMSMERows(@PathVariable Long subActivityId)  {
            MoMSMERowDTO rows = null;
            try {
                rows = tiHclMoMSMEReportService.getMoMSMERowData(subActivityId,"NonTraining");
            } catch (DataException e) {
                return RestControllerBase.error(e);
            }
            return ResponseEntity.ok(rows);
        }


}
