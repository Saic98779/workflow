package com.metaverse.workflow.model;

import com.metaverse.workflow.model.outcomes.ProgramOutcomeTable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Table(name="physical_target")
public class PhysicalTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="physical_target_id")
    private Long physicalTargetId;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    @ManyToOne
    @JoinColumn(name = "outcome_table_id", nullable = false)
    private ProgramOutcomeTable programOutcomeTable;

    @Column(name = "financial_year")
    private String financialYear;

    @Column(name = "q1")
    private Integer q1;

    @Column(name = "q2")
    private Integer q2;

    @Column(name = "q3")
    private Integer q3;

    @Column(name = "q4")
    private Integer q4;

}
