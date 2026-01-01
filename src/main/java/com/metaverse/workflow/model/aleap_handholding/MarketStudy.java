package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "market_study")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarketStudy  extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name = "date_of_study")
    private Date dateOfStudy;

    @OneToMany(
            mappedBy = "marketStudy",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FeasibilityInput> feasibilityInputs;

    @ManyToMany
    @JoinTable(
            name = "counselling_participants",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<Participant> participants;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name ="counselled_by")
    private String counselledBy;

    @Column(name = "counselling_date")
    private Date counsellingDate;

    @Column(name = "counselling_time")
    private String counsellingTime;
}
