package com.metaverse.workflow.model;

//import com.metaverse.workflow.model.aleap_handholding.AleapDesignStudio;
//import com.metaverse.workflow.model.aleap_handholding.BusinessPlanDetails;
import com.metaverse.workflow.model.aleap_handholding.AleapDesignStudio;
import com.metaverse.workflow.model.aleap_handholding.BusinessPlanDetails;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "program_session_file")
public class ProgramSessionFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "program_session_file_id")
    private Long programSessionFileId;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_path")
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "program_session_id")
    private ProgramSession programSession;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "program_expenditure_id")
    private ProgramExpenditure programExpenditure;
    @ManyToOne
    @JoinColumn(name = "bulk_expenditure_id")
    private BulkExpenditure bulkExpenditure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_and_transport", nullable = true) // FK column
    private TravelAndTransport travelAndTransport;

    @ManyToOne
    @JoinColumn(name = "non_training_expenditure")
    private NonTrainingExpenditure nonTrainingExpenditure;

    @ManyToOne
    @JoinColumn(name = "non_training_resource_expenditure_id")
    private NonTrainingResourceExpenditure nonTrainingResourceExpenditure;

    @ManyToOne
    @JoinColumn(name = "benchmarking_study_id")
    private BenchmarkingStudy benchmarkingStudy;

    @ManyToOne
    @JoinColumn(name = "central_data_id")
    private NIMSMECentralData nimsmeCentralData;

    @ManyToOne
    @JoinColumn(name = "nimsme_vendor_details")
    private NIMSMEVendorDetails nimsmeVendorDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulk_id")
    private NonTrainingConsumablesBulk nonTrainingConsumablesBulk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_plan_details_id")
    private BusinessPlanDetails businessPlanDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aleap_design_studio_id")
    private AleapDesignStudio aleapDesignStudio;


}
