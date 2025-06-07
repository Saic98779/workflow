package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "logistics_evaluation")
public class LogisticsEvaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="logistics_evaluation_id")
    private Long logisticsEvaluationId;
    @Column(name="parameter")
    private String parameter;
    @Column(name="rating")
    private Integer rating;
    @Column(name="remarks")
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "monitoring_id")
    private ProgramMonitoringFeedBack programMonitoringFeedBack;

}
