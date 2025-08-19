package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "program_schedule_tracker")
public class ProgramScheduleTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @Column(name = "scheduled_date")
    private Date scheduledDate;

    @Column(name = "previous_date")
    private Date previousDate;

    @Column(name="reason_for_reschedule",length = 1024)
    private String reasonForReschedule;

    @Column(name = "created_on",updatable = false,insertable = true)
    private Date createdOn;

    @Column(name = "updated_on",updatable = true,insertable = false)
    private Date updatedOn;

}
