package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "pre_event_checklist_new")
public class PreEventChecklistNew {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="pre_event_checklist_id")
    private Long preEventChecklistId;
    @Column(name="item")
    private String item;
    @Column(name="status")
    private Boolean status;
    @Column(name="remarks")
    private String remarks;

    @ManyToOne
    @JoinColumn(name = "program_monitoring_id", nullable = false)
    private ProgramMonitoring programMonitoring;
}
