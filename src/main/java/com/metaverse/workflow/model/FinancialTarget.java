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
@Table(name = "financial_target")
public class FinancialTarget {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "financial_target_id")
    private Long financialTargetId;

    @ManyToOne
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    @ManyToOne
    @JoinColumn(name = "outcome_table_id", nullable = false)
    private ProgramOutcomeTable programOutcomeTable;

    @Column(name = "financial_year")
    private String financialYear;

    @Column(name = "q1")
    private Double q1;

    @Column(name = "q2")
    private Double q2;

    @Column(name = "q3")
    private Double q3;

    @Column(name = "q4")
    private Double q4;
}
