package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.RemarkBy;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notification_messages")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notifications notification;

    @Column(nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    private RemarkBy sentBy;   // AGENCY / CALL_CENTER / ADMIN

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
