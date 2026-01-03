package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "TGTPC_NT_Reports")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TGTPCNTReports extends  BaseEntity{

    @Column(name = "sector_name", nullable = false)
    private String sectorName;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "report_submission_date")
    private Date reportSubmissionDate;

    @Column(name = "approval_date")
    private Date approvalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;

}
