package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "program_summary_details")
@EqualsAndHashCode(callSuper = true)
public class ProgramSummaryDetails extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = true, unique = true)
    private Program program;

    @Column(name = "executive_summary", columnDefinition = "TEXT")
    private String executiveSummary;

    @Column(name = "college_details", columnDefinition = "TEXT")
    private String collegeDetails;
}