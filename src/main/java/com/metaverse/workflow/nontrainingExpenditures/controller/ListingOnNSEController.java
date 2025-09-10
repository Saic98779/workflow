package com.metaverse.workflow.nontrainingExpenditures.controller;

import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.nontrainingExpenditures.Dto.ListingOnNSERequest;
import com.metaverse.workflow.nontrainingExpenditures.service.ListingOnNSEService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/listing-on-nse")
@RequiredArgsConstructor
public class ListingOnNSEController {

    private final ListingOnNSEService listingOnNSEService;

    @PostMapping("/save")
    public ResponseEntity<?> createListingOnNSE(@RequestBody ListingOnNSERequest request) {
        try {
            return ResponseEntity.ok(listingOnNSEService.createListingOnNSE(request));
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
    public ResponseEntity<?> deleteListingOnNSE(@PathVariable Long listingOnNSEId) {
        try {
            return ResponseEntity.ok(listingOnNSEService.deleteListingOnNSE(listingOnNSEId));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
