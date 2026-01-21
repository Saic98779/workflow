package com.metaverse.workflow.model.tgtpc_handholding;

import com.metaverse.workflow.model.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
@Entity
@Table(name = "tgtpc_handholding_support")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TGTPCHandholdingSupport extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="tgtpc_handholding_support_id")
    private Long tgtpcHandholdingSupportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;

    @Column(name="handholding_support_by")
    private String handholdingSupportBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "tgtpc_handholding_support_participants",
            joinColumns = @JoinColumn(name = "tgtpc_handholding_support_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<Participant> participants;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "tgtpc_handholding_support_influenced_participants",
            joinColumns = @JoinColumn(name = "tgtpc_handholding_support_id"),
            inverseJoinColumns = @JoinColumn(name = "influenced_participant_id")
    )
    private List<InfluencedParticipant> influencedParticipants;

    @Column(name = "handholding_date")
    private Date handholdingDate;

    @Column(name = "handholding_time")
    private String handholdingTime;

    //Packaging Standards support sub activity
    @Column(name = "packaging_standards_support_details", columnDefinition = "TEXT")
    private String packagingStandardsSupportDetails;

    //Branding Support sub activity
    @Column(name = "branding_support_details", columnDefinition = "TEXT")
    private String brandingSupportDetails;

}
