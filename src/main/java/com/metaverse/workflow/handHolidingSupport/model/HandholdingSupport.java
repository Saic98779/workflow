package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.util.List;

@Entity
@Table(name = "handholding_support")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HandholdingSupport extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private SubActivity subActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counsellor_registration_id")
    private CounsellorRegistration counsellor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @Column(name = "counselling_date")
    private LocalDate counsellingDate;

    @Column(name = "subject_delivered", length = 1000)
    private String subjectDelivered;

    @Column(name = "original_idea", length = 1000)
    private String originalIdea;

    @Column(name = "final_idea", length = 1000)
    private String finalIdea;

    @OneToMany(
            mappedBy = "handholdingSupport",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<BusinessPlanDetails> businessPlans;

    @OneToMany(
            mappedBy = "handholdingSupport",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<MarketStudy> marketStudies;

    @OneToMany(
            mappedBy = "handholdingSupport",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<SectorAdvisoryDetails> sectorAdvisoryDetails;

    @OneToMany(
            mappedBy = "handholdingSupport",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<GovtSchemeApplication> govtSchemeApplications;
}


