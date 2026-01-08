package com.metaverse.workflow.model.tgtpc_handholding;


import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tgtpc_iec_registration_certification")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IECRegistrationCertification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tgtpc_handholding_support_id", nullable = false)
    private TGTPCHandholdingSupport tgtpcHandholdingSupport;

    @Column(name = "support_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private SupportType supportType;

    @Column(name = "iec_registration_number")
    private String iecRegistrationNumber;

    @Column(name = "registration_date")
    private Date registrationDate;

    @Column(name = "certification_name")
    private String certificationName;

    @Column(name = "certificate_number")
    private String certificateNumber;

    @Column(name = "certificate_date")
    private Date certificateDate;

    public enum SupportType {
        IEC_REGISTRATION,
        IEC_CERTIFICATION
    }
}

