package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.model.Organization;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "formalisation_compliance")
@Getter
@Setter
public class FormalisationCompliance{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "formalisation_compliance_id")
    private Long formalisationComplianceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private NonTrainingActivity nonTrainingActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;


    @Column(name = "document_path", length = 500)
    private String documentPath;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;
}
