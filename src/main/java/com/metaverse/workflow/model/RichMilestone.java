package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "rich_milestone")
@Data
public class RichMilestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rich_milestone_id")
    private Long richMilestoneId;

    @Column(name = "payment_iteration")
    private String paymentIteration;

    @Column(name = "payment_milestone")
    private String paymentMilestone;

    @Column(name = "payment_percentage")
    private Double paymentPercentage;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JoinColumn(name = "nonTrainingExpenditure")
    @JsonIgnore
    private NonTrainingExpenditure nonTrainingExpenditure;

    @ManyToOne
    @JoinColumn(name = "travelAndTransport")
    @JsonIgnore
    private TravelAndTransport travelAndTransport;
}
