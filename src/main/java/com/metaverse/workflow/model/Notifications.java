package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.NotificationRecipientType;
import com.metaverse.workflow.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    /** Receiver (can be call center agent, admin, or assignee) */
    @ManyToOne
    @JoinColumn(name = "receiver_id", referencedColumnName = "user_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    // ----------------------------------------------------------------------
    // MESSAGES (Unified Remarks Handling)
    // ----------------------------------------------------------------------

    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationMessage> messages = new ArrayList<>();

    /** For global sorting of notifications */
    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    public void updateLastMessageTime() {
        this.lastMessageAt = LocalDateTime.now();
    }

    // ----------------------------------------------------------------------

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    @Column(name = "date_of_fix")
    private LocalDateTime dateOfFix;

    @Column(name = "date_of_closure")
    private LocalDateTime dateOfClosure;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type", nullable = false)
    private NotificationRecipientType recipientType;

    private Boolean isRead;
}
