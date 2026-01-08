package com.metaverse.workflow.model.tgtpc_handholding;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tgtpc_local_testing_lab_attachment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocalTestingLabAttachment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tgtpc_handholding_support_id", nullable = false)
    private TGTPCHandholdingSupport tgtpcHandholdingSupport;

    @Column(name = "lab_or_cfc_name",columnDefinition = "TEXT")
    private String labOrCfcName;

    @Column(name = "purpose_of_attachment",columnDefinition = "TEXT")
    private String purposeOfAttachment;

    @Column(name = "date_of_attachment")
    private Date dateOfAttachment;

}

