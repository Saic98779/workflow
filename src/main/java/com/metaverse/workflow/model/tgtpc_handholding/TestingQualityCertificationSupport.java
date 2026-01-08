package com.metaverse.workflow.model.tgtpc_handholding;



import com.metaverse.workflow.model.AuditEntity;
import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tgtpc_testing_quality_certification_support")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestingQualityCertificationSupport extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="testing_id")
    protected Long testingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tgtpc_handholding_support_id", nullable = false)
    private TGTPCHandholdingSupport tgtpcHandholdingSupport;

    @Column(name = "testing_agency_name")
    private String testingAgencyName;

    @Column(name = "date_of_test")
    private Date dateOfTest;

    @Column(name = "product_tested", length = 255)
    private String productTested;

    @Column(name = "test_results_date")
    private Date testResultsDate;

    @Column(name = "certification_or_test_findings", columnDefinition = "TEXT")
    private String certificationOrTestFindings;

    @Column(name = "test_result_file_path", length = 500)
    private String testResultFilePath;
}
