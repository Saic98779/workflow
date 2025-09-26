package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Table(name = "program_reschedule_data")
public class ProgramReschedule extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @Column(name = "old_start_date")
    private Date oldStartDate;

    @Column(name = "new_start_date")
    private Date newStartDate;
}

