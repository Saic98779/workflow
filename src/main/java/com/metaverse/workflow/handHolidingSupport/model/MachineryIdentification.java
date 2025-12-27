package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "machinery_identification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MachineryIdentification extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technology_support_id", nullable = false)
    private TechnologySupport technologySupport;

    @Column(columnDefinition = "TEXT")
    private String requirement;

    private String existingMachinery;

    private String suggestedMachinery;

    private String manufacturer;

    private LocalDate groundingDate;

    private String placeOfInstallation;

    private Double costOfMachinery;
}

