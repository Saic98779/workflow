package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "mo_msme_report_actual")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMSMEReportActual {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actual_id")
    private Long actualId;

    @Column(name = "financial_year")
    private String financialYear;

    @Column(name = "month")
    private String month;

    @Column(name = "physical_achievement")
    private Double physicalAchievement;

    @Column(name = "financial_achievement")
    private Double financialAchievement;

    @Column(name = "total")
    private Integer total;

    @Column(name = "women")
    private Integer women;

    @Column(name = "sc")
    private Integer sc;

    @Column(name = "st")
    private Integer st;

    @Column(name = "oc")
    private Integer oc;

    @Column(name = "bc")
    private Integer bc;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on", insertable = false)
    private Date updatedOn;
}
