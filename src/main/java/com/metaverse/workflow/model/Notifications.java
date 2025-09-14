package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
@Table(name = "notifications")
public class Notifications extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    protected Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "agency_id")
    private Long agencyId;

    @Column(name="user_type")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(name = "source_id")
    private Long sourceId;       // sourceId

    @Column(name = "screen_name")
    private String screenName;      //Screen name

    @Column(name = "message")
    private String message;

    private Boolean readRecipients;
}

