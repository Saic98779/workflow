package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name="program_session_file")
public class ProgramSessionFile {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="program_session_file_id")
    private Long programSessionFileId;
    @Column(name="file_type")
    private String fileType;
    @Column(name="file_path")
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
    @ManyToOne
    @JoinColumn(name = "budget_id")
    private OtherTrainingBudget otherTrainingBudget;

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

}
