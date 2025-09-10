package com.metaverse.workflow.nontrainingExpenditures.service;


import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.ListingOnNSE;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.nontrainingExpenditures.Dto.ListingOnNSERequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.ListingOnNSEResponse;
import com.metaverse.workflow.nontrainingExpenditures.repository.ListingOnNSERepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingOnNSEService {
    private final ListingOnNSERepository listingOnNSERepository;
    private final NonTrainingSubActivityRepository subActivityRepository;


    public WorkflowResponse createListingOnNSE(ListingOnNSERequest request) throws DataException {
        ListingOnNSE entity;
        if (listingOnNSERepository.existsById(request.getListingOnNSEId())) {
            entity = listingOnNSERepository.findById(request.getListingOnNSEId())
                    .orElseThrow(() -> new DataException("listing On NSE not found", "NOT_FOUND", 400));
            entity = ListingOnNSEMapper.mapToListingOnNSEUpdate(request, entity);
        } else {
            NonTrainingSubActivity subActivity = subActivityRepository.findById(request.getSubActivityId())
                    .orElseThrow(() -> new DataException("SubActivity not found", "SUB_ACTIVITY_NOT_FOUND", 400));
            entity = ListingOnNSEMapper.mapToListingOnNSEReq(request, subActivity);
        }
        return WorkflowResponse.builder()
                .status(201)
                .message("ListingOnNSE created successfully")
                .data(ListingOnNSEMapper.mapToListingOnNSERes(listingOnNSERepository.save(entity)))
                .build();
    }


    public WorkflowResponse getListingOnNSEBySubActivityId(Long subActivityId) throws DataException {
        List<ListingOnNSE> listingOnNSEList = listingOnNSERepository.findByNonTrainingSubActivity_SubActivityId(subActivityId)
                .orElseThrow(() -> new DataException("ListingOnNSE not found for subActivityId: " + subActivityId,
                        "LISTING_ON_NSE_NOT_FOUND", 404));
        List<ListingOnNSEResponse> listingOnNSEResponses = listingOnNSEList.stream().map(ListingOnNSEMapper::mapToListingOnNSERes).toList();
        return WorkflowResponse.builder()
                .status(200)
                .message("ListingOnNSE fetched successfully")
                .data(listingOnNSEResponses)
                .build();
    }


    public WorkflowResponse getListingOnNSEById(Long listingOnNSEId) throws DataException {
        ListingOnNSE entity = listingOnNSERepository.findById(listingOnNSEId)
                .orElseThrow(() -> new DataException("ListingOnNSE not found", "LISTING_ON_NSE_NOT_FOUND", 404));

        return WorkflowResponse.builder()
                .status(200)
                .message("ListingOnNSE fetched successfully")
                .data(ListingOnNSEMapper.mapToListingOnNSERes(entity))
                .build();
    }

    public WorkflowResponse deleteListingOnNSE(Long listingOnNSEId) throws DataException {
        ListingOnNSE entity = listingOnNSERepository.findById(listingOnNSEId)
                .orElseThrow(() -> new DataException("ListingOnNSE not found", "LISTING_ON_NSE_NOT_FOUND", 404));

        listingOnNSERepository.delete(entity);

        return WorkflowResponse.builder()
                .status(200)
                .message("ListingOnNSE deleted successfully")
                .build();
    }


}
