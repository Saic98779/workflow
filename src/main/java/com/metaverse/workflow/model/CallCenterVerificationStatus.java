package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="call_center_verification_status")
public class CallCenterVerificationStatus {
    @Id
    @Column(name="cc_verification_status_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ccVerificationStatusId;

    @Column(name="cc_verification_details")
    private String verificationDetails;

    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
