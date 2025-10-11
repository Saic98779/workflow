package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "mo_msme_report")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMSMEReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mo_msme_activity_id")
    private Long moMSMEActivityId;

    @Column(name = "intervention")
    private String intervention;//agency

    @Column(name = "component")
    private String component;//Activity

    @Column(name = "mo_msme_activity")
    private String moMSMEActivity;//subActivity

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on", insertable = false)
    private Date updatedOn;
}
