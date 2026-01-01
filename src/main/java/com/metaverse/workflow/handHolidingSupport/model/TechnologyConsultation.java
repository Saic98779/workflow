package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "technology_consultation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TechnologyConsultation extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technology_support_id", nullable = false)
    private TechnologySupport technologySupport;

    @Column(columnDefinition = "TEXT")
    private String consultationDetails;
}

