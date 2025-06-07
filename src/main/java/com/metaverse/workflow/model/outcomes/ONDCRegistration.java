package com.metaverse.workflow.model.outcomes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "outcome_ondc_registration")
public class ONDCRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ondc_registration_id")
    private Long ondcRegistrationId;
    @Column(name = "ondc_registration_No")
    private String ondcRegistrationNo;
    @Column(name = "ondc_registration_Date")
    private Date ondcRegistrationDate;
    @Column(name = "created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
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
    @Column(name="Influenced")
    Boolean isInfluenced;
    @OneToMany(mappedBy = "ondcRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ONDCTransaction> ondcTransactionList;

}
