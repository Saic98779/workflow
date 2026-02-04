package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "non_training_consumables_transactions")
public class NonTrainingConsumablesTransactions extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "program_id", nullable = true)
    private Program program;

    @Column(name = "date_of_utilisation")
    private Date dateOfUtilisation;

    @Column(name = "quantity_of_utilisation")
    private Integer quantityOfUtilisation;

    @Column(name = "no_of_trainees_utilised")
    private Integer noOfTraineesUtilised;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulk_id")
    private NonTrainingConsumablesBulk nonTrainingConsumablesBulk;

}

