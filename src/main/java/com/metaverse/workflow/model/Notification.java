package com.metaverse.workflow.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    protected Long id;

    @ManyToOne
    @JoinColumn(name = "program")
    private Program program;

    @Column(name="call_center_id")
    private Long callCenterId;

    @Column(name="user_type")
    // Visible to
    private String userType;

    @Column(name = "message")
    private String message;

    @CreatedDate
    @Column(name = "created_timestamp", updatable = false)
    protected LocalDateTime createdTimestamp;

    @LastModifiedDate
    @Column(name = "last_modified")
    protected LocalDateTime lastModified;

    @Version
    protected Integer version;
}
