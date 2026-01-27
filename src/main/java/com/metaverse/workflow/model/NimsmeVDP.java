package com.metaverse.workflow.model;

import com.metaverse.workflow.common.enums.ExpressionOfInterest;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "nimsme_vdp")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NimsmeVDP extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nimsme_vdp_id")
    private Long nimsmeVdpId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;

    // Seller Details
    @Column(name = "seller_name")
    private String sellerName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_organization_id")
    private Organization sellerOrganization;

    @Column(name = "product_service")
    private String productService;

    @Column(name = "product_category")
    private String productCategory;

    @Column(name = "specifications_features", columnDefinition = "TEXT")
    private String specificationsFeatures;

    @Column(name = "available_volume")
    private Double availableVolume;

    @Column(name = "unit")
    private String unit;

    @Column(name = "price_quotation")
    private Double priceQuotation;

    // Buyer Details
    @Column(name = "buyer_name")
    private String buyerName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_organization_id")
    private Organization buyerOrganization;

    @Column(name = "buyer_comments", columnDefinition = "TEXT")
    private String buyerComments;

    // Engagement Details
    @Enumerated(EnumType.STRING)
    @Column(name = "expression_of_interest", nullable = false)
    private ExpressionOfInterest expressionOfInterest;

    @Column(name = "follow_up_required")
    private Boolean followUpRequired;

    @Column(name = "responsible_officer")
    private String responsibleOfficer;

    @Column(name = "remarks_notes", columnDefinition = "TEXT")
    private String remarksNotes;

    @Column(name = "upload_participant_details")
    private String uploadParticipantDetails;
}
