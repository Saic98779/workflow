package com.metaverse.workflow.model.outcomes;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "outcome_udyam_registration")
public class UdyamRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "udyam_registration_id")
    private Long udyamRegistrationId;

    @Column(name="udyam_registration_date")
    private Date udyamRegistationDate;

    @Column(name="udyam_registration_no")
    private String udyamRegistrationNo;

    @Column(name="Influenced")
    private Boolean isInfluenced;

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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "influenced_id")
    private InfluencedParticipant influencedParticipant;
}
