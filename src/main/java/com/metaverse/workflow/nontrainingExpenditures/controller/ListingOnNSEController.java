package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.ListingOnNSERequest;
import com.metaverse.workflow.nontrainingExpenditures.service.ListingOnNSEService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/listing-on-nse")
@RequiredArgsConstructor
@Tag(name = "Non Training", description = "Listing on NSE APIs")
public class ListingOnNSEController {

    private final ListingOnNSEService listingOnNSEService;
    private final ActivityLogService logService;

    @PostMapping("/save")
    public ResponseEntity<?> createListingOnNSE(@RequestBody ListingOnNSERequest request, Principal principal) {
        try {
            WorkflowResponse response = listingOnNSEService.createListingOnNSE(request);
            logService.logs(principal.getName(), "SAVE", "Listing on NSE created successfully", "ListingOnNSE", "/listing-on-nse/save");
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/sub-activity/{subActivityId}")
    public ResponseEntity<?> getListingOnNSEBySubActivityId(@PathVariable Long subActivityId) {
        try {
            return ResponseEntity.ok(listingOnNSEService.getListingOnNSEBySubActivityId(subActivityId));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/{listingOnNSEId}")
    public ResponseEntity<?> getListingOnNSEById(@PathVariable Long listingOnNSEId) {
        try {
            return ResponseEntity.ok(listingOnNSEService.getListingOnNSEById(listingOnNSEId));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @DeleteMapping("/delete/{listingOnNSEId}")
    public ResponseEntity<?> deleteListingOnNSE(@PathVariable Long listingOnNSEId,Principal principal) {
        try {
            WorkflowResponse response =listingOnNSEService.deleteListingOnNSE(listingOnNSEId);
            logService.logs(principal.getName(), "DELETE", "Listing on NSE deleted successfully | ID: " + listingOnNSEId, "ListingOnNSE", "/listing-on-nse/delete/"+listingOnNSEId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
