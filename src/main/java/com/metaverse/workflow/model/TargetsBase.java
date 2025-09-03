package com.metaverse.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class TargetsBase {

    @Column(name = "q1_target")
    private Long q1Target;
    @Column(name = "q2_target")
    private Long q2Target;
    @Column(name = "q3_target")
    private Long q3Target;
    @Column(name = "q4_target")
    private Long q4Target;

    @Column(name = "q1_budget")
    private Double q1Budget;
    @Column(name = "q2_budget")
    private Double q2Budget;
    @Column(name = "q3_budget")
    private Double q3Budget;
    @Column(name = "q4_budget")
    private Double q4Budget;
}


