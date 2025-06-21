package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@Table(name = "outcome_copy_rights")
public class CopyRights {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "copy_rights_id")
    private Long copyRightsId;

    @Column(name = "date_of_application_filed")
    private Date dateOfApplicationFiled;

    @Column(name = "type_of_intellectual_work_registered")
    private String typeOfIntellectualWorkRegistered;

    @Column(name = "registration_certificate_received_date")
    private Date registrationCertificateReceivedDate;

    @Column(name = "registration_certificate_number")
    private String registrationCertificateNumber;

    @Column(name = "number_of_products_protected")
    private Integer numberOfProductsProtected;

    @Column(name = "name_of_product_protected")
    private String nameOfProductProtected;

    @Column(name = "revenue_from_copyrighted_material")
    private Double revenueFromCopyrightedMaterial;

    @Column(name = "market_value_after_copyright")
    private Double marketValueAfterCopyright;

    @Column(name = "is_influenced")
    private Boolean isInfluenced;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
    @Column(name = "created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
