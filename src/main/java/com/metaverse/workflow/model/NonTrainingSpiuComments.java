package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "non_training_spiu_comments")
public class NonTrainingSpiuComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private String remarks;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy HH:mm:ss")
    private LocalDateTime remarkTimestamp;

    @ManyToOne
    @JoinColumn(name = "nonTrainingExpenditure")
    @JsonIgnore
    private NonTrainingExpenditure nonTrainingExpenditure;

    @ManyToOne
    @JoinColumn(name = "benchmarkingStudy")
    @JsonIgnore
    private BenchmarkingStudy benchmarkingStudy;

    @ManyToOne
    @JoinColumn(name = "nimsmeCentralData")
    @JsonIgnore
    private NIMSMECentralData nimsmeCentralData;

    @ManyToOne
    @JoinColumn(name = "nimsmeContentDetails")
    @JsonIgnore
    private NIMSMEContentDetails nimsmeContentDetails;

    @ManyToOne
    @JoinColumn(name = "nimsmeVendorDetails")
    @JsonIgnore
    private NIMSMEVendorDetails nimsmeVendorDetails;

    @ManyToOne
    @JoinColumn(name = "nonTrainingResourceExpenditure")
    @JsonIgnore
    private NonTrainingResourceExpenditure nonTrainingResourceExpenditure;

    @ManyToOne
    @JoinColumn(name = "travelAndTransport")
    @JsonIgnore
    private TravelAndTransport travelAndTransport;

//    @ManyToOne
//    @JoinColumn(name = "bulkExpenditureTransaction", referencedColumnName = "bulk_expenditure_transaction_id")
//    @JsonIgnore
//    private BulkExpenditureTransaction bulkExpenditureTransaction;

    @Transient
    public String getFormattedRemark() {
        if (remarkTimestamp == null || remarks == null) return "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
        return remarkTimestamp.format(formatter) + ": " + remarks;
    }

    @PrePersist
    public void prePersist() {
        this.remarkTimestamp = LocalDateTime.now();
    }

}
