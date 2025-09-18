package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "listing_on_nse")
public class ListingOnNSE {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long listingOnNSEId;

    @Column(name = "msme_name", nullable = false, length = 255)
    private String nameOfTheMsmeIdentified;

    @Column(name = "date_of_identification")
    private Date dateOfIdentification;

    @Column(name = "loan_amount_Provided")
    private Double amountOfLoanProvided;

    @Column(name = "purpose", length = 500)
    private String purpose;

    @Column(name = "date_of_loan")
    private Date dateOfLoan;

    @Column(name = "date_of_listing")
    private Date dateOfListing;

    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @Column(name = "updated_on", insertable = false, updatable = true)
    private Date updatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id")
    private NonTrainingSubActivity nonTrainingSubActivity;
}
