package com.metaverse.workflow.nontrainingExpenditures.Dto;

import lombok.Data;

@Data
public class ListingOnNSERequest {
    private Long listingOnNSEId;
    private String nameOfTheMsmeIdentified;
    private String dateOfIdentification;
    private Double amountOfLoanProvided;
    private String purpose;
    private String dateOfLoan;
    private String dateOfListing;
    private Long subActivityId;

}
