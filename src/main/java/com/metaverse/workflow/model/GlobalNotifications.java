package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.NotificationRecipientType;
import com.metaverse.workflow.enums.RemarkBy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "global_notifications")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalNotifications extends BaseEntity {

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "remark_by")
    private RemarkBy remarkBy; // who created it (AGENCY, CALL_CENTER, ADMIN)

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type")
    private NotificationRecipientType recipientType; // who receives it

    @Column(name = "is_read")
    private Boolean isRead = false;

    @Column(name = "date_of_notification")
    private Date dateOfNotification;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "user_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "recipient_user_id", referencedColumnName = "user_id")
    private User recipientUser;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;
}