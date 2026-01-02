package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.Organization;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "govt_scheme_application")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GovtSchemeApplication extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private NonTrainingActivity nonTrainingActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "application_no")
    private String applicationNo;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "application_date")
    private Date applicationDate;

    @Column(name = "time")
    private String time;

    @Column(name = "sanction_details", length = 1000)
    private String sanctionDetails;

    @Column(name = "sanction_date")
    private Date sanctionDate;

    @Column(name = "sanction_amount")
    private Double sanctionedAmount;

    @Column(name = "details")
    private String details;

    public enum ApplicationStatus {
        SUBMITTED,
        UNDER_PROCESS,
        APPROVED,
        REJECTED
    }
}