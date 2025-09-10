package com.metaverse.workflow.nontrainingExpenditures.service;


import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.ListingOnNSE;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.nontrainingExpenditures.Dto.ListingOnNSERequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.ListingOnNSEResponse;

public class ListingOnNSEMapper {

    private static final String DATE_PATTERN = "dd-MM-YYYY";


    public static ListingOnNSE mapToListingOnNSEReq(ListingOnNSERequest request, NonTrainingSubActivity subActivity) {
        if (request == null) {
            return null;
        }
        ListingOnNSE entity = new ListingOnNSE();

        entity.setNameOfTheMsmeIdentified(request.getNameOfTheMsmeIdentified());
        entity.setDateOfIdentification(DateUtil.covertStringToDate(request.getDateOfIdentification()));
        entity.setAmountOfLoanProvided(request.getAmountOfLoanProvided());
        entity.setPurpose(request.getPurpose());
        entity.setDateOfLoan(DateUtil.covertStringToDate(request.getDateOfLoan()));
        entity.setDateOfListing(DateUtil.covertStringToDate(request.getDateOfListing()));
        entity.setNonTrainingSubActivity(subActivity);


        return entity;
    }


    public static ListingOnNSE mapToListingOnNSEUpdate(ListingOnNSERequest request, ListingOnNSE entity) {
        if (request == null || entity == null) {
            return null;
        }
        entity.setNameOfTheMsmeIdentified(request.getNameOfTheMsmeIdentified() != null ? request.getNameOfTheMsmeIdentified() : entity.getNameOfTheMsmeIdentified());
        entity.setDateOfIdentification(request.getDateOfIdentification() != null ? DateUtil.covertStringToDate(request.getDateOfIdentification()) : entity.getDateOfIdentification());
        entity.setAmountOfLoanProvided(request.getAmountOfLoanProvided() != null ? request.getAmountOfLoanProvided() : entity.getAmountOfLoanProvided());
        entity.setPurpose(request.getPurpose() != null ? request.getPurpose() : entity.getPurpose());
        entity.setDateOfLoan(request.getDateOfLoan() != null ? DateUtil.covertStringToDate(request.getDateOfLoan()) : entity.getDateOfLoan());
        entity.setDateOfListing(request.getDateOfListing() != null ? DateUtil.covertStringToDate(request.getDateOfListing()) : entity.getDateOfListing());
        return entity;
    }


    public static ListingOnNSEResponse mapToListingOnNSERes(ListingOnNSE entity) {
        if (entity == null) {
            return null;
        }
        ListingOnNSEResponse response = new ListingOnNSEResponse();

        response.setListingOnNSEId(entity.getListingOnNSEId());
        response.setNameOfTheMsmeIdentified(entity.getNameOfTheMsmeIdentified());
        response.setDateOfIdentification(DateUtil.dateToString(entity.getDateOfIdentification(), DATE_PATTERN));
        response.setAmountOfLoanProvided(entity.getAmountOfLoanProvided());
        response.setPurpose(entity.getPurpose());
        response.setDateOfLoan(DateUtil.dateToString(entity.getDateOfLoan(), DATE_PATTERN));
        response.setDateOfListing(DateUtil.dateToString(entity.getDateOfListing(), DATE_PATTERN));

        if (entity.getNonTrainingSubActivity() != null) {
            response.setSubActivityId(entity.getNonTrainingSubActivity().getSubActivityId());
            response.setSubActivityName(entity.getNonTrainingSubActivity().getSubActivityName());
        }

        return response;
    }
}
