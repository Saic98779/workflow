package com.metaverse.workflow.model;

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
@NoArgsConstructor  // ✅ required for JPA
@AllArgsConstructor // ✅ optional, but useful
public abstract class TargetsBase {

    private Long q1Target;
    private Long q2Target;
    private Long q3Target;
    private Long q4Target;

    private Double q1Budget;
    private Double q2Budget;
    private Double q3Budget;
    private Double q4Budget;
}


