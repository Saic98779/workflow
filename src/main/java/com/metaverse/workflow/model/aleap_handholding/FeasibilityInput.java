package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "feasibility_input")
@Getter
@Setter
public class FeasibilityInput extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_study_id", nullable = false)
    private MarketStudy marketStudy;

    @Column(name = "input_details", columnDefinition = "TEXT")
    private String inputDetails;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type")
    private SourceType source;

    @Column(name = "sector")
    private String sector;

    @Column(name = "feasibility_activity")
    private String feasibilityActivity;

    public enum SourceType {
        PRIMARY,
        SECONDARY
    }
}
