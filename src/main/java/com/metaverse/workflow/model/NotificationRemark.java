package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.RemarkBy;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notification_remarks")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRemark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "remark_text", length = 2000, nullable = false)
    private String remarkText;

    @ManyToOne
    @JoinColumn(name = "notification_id", nullable = false)
    private Notifications notification;

    @Enumerated(EnumType.STRING)
    @Column(name = "remark_by", nullable = false)
    private RemarkBy remarkBy;

    @Column(name = "remarked_at", nullable = false)
    private LocalDateTime remarkedAt;
}

