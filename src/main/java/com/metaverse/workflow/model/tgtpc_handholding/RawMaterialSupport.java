package com.metaverse.workflow.model.tgtpc_handholding;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "tgtpc_raw_material_support")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawMaterialSupport extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tgtpc_handholding_support_id", nullable = false)
    private TGTPCHandholdingSupport tgtpcHandholdingSupport;

    @Column(name = "raw_material_details", columnDefinition = "TEXT")
    private String rawMaterialDetails;

    @Column(name = "first_date_of_supply")
    private Date firstDateOfSupply;

    @Column(name = "cost")
    private Double cost;

}
