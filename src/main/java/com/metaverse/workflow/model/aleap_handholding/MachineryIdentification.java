package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "machinery_identification")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MachineryIdentification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(columnDefinition = "TEXT")
    private String requirement;

    @Column(name = "existing_machinery")
    private String existingMachinery;

    @Column(name = "suggested_machinery")
    private String suggestedMachinery;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "grounding_date")
    @Temporal(TemporalType.DATE)
    private Date groundingDate;

    @Column(name = "place_of_installation")
    private String placeOfInstallation;

    @Column(name = "cost_of_machinery")
    private Double costOfMachinery;

}

