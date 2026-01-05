package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.InfluencedParticipant;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
@Builder
@Entity
@Table(name = "sector_advisory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SectorAdvisory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name="advise_details" ,length = 1000)
    private String adviseDetails;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "sector_advisory_participants",      // separate join table
            joinColumns = @JoinColumn(name = "sector_advisory_id"),   // FK to SectorAdvisory.id
            inverseJoinColumns = @JoinColumn(name = "participant_id") // FK to Participant.participant_id
    )
    private List<Participant> participants;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "sector_advisory_influenced_participants",
            joinColumns = @JoinColumn(name = "sector_advisory_id"),
            inverseJoinColumns = @JoinColumn(name = "influenced_participant_id")
    )
    private List<InfluencedParticipant> influencedParticipants;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name ="counselled_by")
    private String counselledBy;

    @Column(name = "counselling_date")
    private Date counsellingDate;

    @Column(name = "counselling_time")
    private String counsellingTime;
}
