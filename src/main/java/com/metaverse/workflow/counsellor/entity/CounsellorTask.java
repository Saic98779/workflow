package com.metaverse.workflow.counsellor.entity;

import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entity representing a counsellor's scheduled task (daily uploads / activities).
 * Basic fields: date, calendar, timeFrom, timeTo, taskType (DDL), remarks.
 */
@Entity
@Table(name = "counsellor_tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounsellorTask extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "counsellor_id")
    private User counsellor;

    private LocalDate taskDate;

    private String calendar;

    private LocalTime timeFrom;

    private LocalTime timeTo;

    @Enumerated(EnumType.STRING)
    private TaskType taskType;

    @Column(length = 2000)
    private String remarks;

    private String status;

    public enum TaskType {
        COUNSELLING,
        TRAVELLING,
        GIVING_TRAINING,
        ATTENDING_TRAINING,
        ARRANGING_TRAINING,
        ANALYTICS
    }
}

