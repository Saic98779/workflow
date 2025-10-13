package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "momsme_report_submitted")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoMSMEReportSubmitted extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "submitted_id")
    private Long submittedId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mo_msme_activity_id", nullable = false)
    private MoMSMEReport moMSMEReport;

    @Column(name = "financial_year", nullable = false)
    private String financialYear;

    @OneToMany(mappedBy = "moMSMEReportSubmitted", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MoMSMEReportSubmittedMonthly> monthlyReports;

    @OneToOne(mappedBy = "moMSMEReportSubmitted", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MoMSMEReportSubmittedQuarterly quarterlyReport;
}
