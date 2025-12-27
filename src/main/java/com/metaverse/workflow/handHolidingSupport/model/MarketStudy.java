package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "market_study")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarketStudy extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name = "date_of_study")
    private LocalDate dateOfStudy;

    @OneToMany(
            mappedBy = "marketStudy",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<FeasibilityInput> feasibilityInputs;
}
