package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
public class Notifications {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    protected Long id;

    @Column(name = "application_no")
    private String applicationNo;

    @Column(name = "application_status")
    private String applicationStatus;

    @Column(name="user_type")
    private String userType;

    @Column(name = "source_id")
    private Long sourceId;       // sourceId

    @Column(name = "screen_name")
    private String screenName;      //Screen name

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

    private Boolean readRecipients;

    private String userId;

    private String managerId;

    private String district;
}

