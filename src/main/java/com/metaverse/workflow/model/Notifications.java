package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.NotificationRecipientType;
import com.metaverse.workflow.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "notifications")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Notifications extends BaseEntity {

    @Column(name = "date_of_notification")
    private LocalDateTime dateOfNotification;

    @Column(name = "date_of_first_notification")
    private LocalDateTime dateOfFirstNotification;

    @ManyToOne
    @JoinColumn(name = "call_center_agent_id", referencedColumnName = "user_id")
    private User callCenterAgent;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    // Remarks provided by Agency
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    @Filter(name = "agencyRemarks", condition = "remark_by = 'AGENCY'")
    @OrderBy("remarkedAt DESC")
    private List<NotificationRemark> remarksByAgency = new ArrayList<>();

    // Remarks provided by Call Center
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    @Filter(name = "callCenterRemarks", condition = "remark_by = 'CALL_CENTER'")
    @OrderBy("remarkedAt DESC")
    private List<NotificationRemark> remarksByCallCenter = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    @Column(name = "date_of_fix")
    private LocalDateTime dateOfFix;

    @Column(name = "date_of_closure")
    private LocalDateTime dateOfClosure;

    // Notifications that intended to be sent to different recipient types
    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type", nullable = false)
    private NotificationRecipientType recipientType;
}
