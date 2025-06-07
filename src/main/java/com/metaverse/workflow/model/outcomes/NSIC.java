package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "outcome_nisc")
public class NSIC {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "nisc_id")
    private Long nsicId;

    @Column(name = "govt_agency_procured")
    private String govtAgencyProcured;

    @Column(name = "date_of_procurement")
    private Date dateOfProcurement;

    @Column(name = "type_of_product_supplied")
    private String typeOfProductSupplied;


    @Column(name = "value_of_procurement")
    private Double valueOfProcurement;

    @Column(name = "cost_savings_tender")
    private Double costSavingsTender;

    @Column(name = "Influenced")
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
