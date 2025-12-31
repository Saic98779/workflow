package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "formalisation_compliance")
@Getter
@Setter
public class FormalisationCompliance extends BaseEntity {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id")
    private HandholdingSupport handholdingSupport;

    @Column(name = "document_path",  columnDefinition = "TEXT")
    private String documentPath;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;
}
