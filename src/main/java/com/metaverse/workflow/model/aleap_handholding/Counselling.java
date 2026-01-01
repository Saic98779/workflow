package com.metaverse.workflow.model.aleap_handholding;
import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "counselling")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Counselling extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name = "subject_delivered", columnDefinition = "TEXT")
    private String subjectDelivered;

    @Column(name = "original_idea", columnDefinition = "TEXT")
    private String originalIdea;

    @Column(name = "final_idea", columnDefinition = "TEXT")
    private String finalIdea;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "counselling_participants",
            joinColumns = @JoinColumn(name = "counselling_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<Participant> participants;



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
