package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.*;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private NonTrainingActivity nonTrainingActivity;

    @Column(name="handholding_support_type")
    private String handholdingSupportType;

    @OneToMany(
            mappedBy = "handholdingSupport",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Counselling> counsellings;

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
    private List<SectorAdvisory> sectorAdvisories;

}


