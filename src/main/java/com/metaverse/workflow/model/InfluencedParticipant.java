package com.metaverse.workflow.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "influenced_participant")
public class InfluencedParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "influenced_id")
    private Long influencedId;

    @Column(name = "participant_name", nullable = false)
    private String participantName;

    @Column(name = "gender")
    private Character gender;

    @Column(name = "category")
    private String category;

    @Column(name = "disability")
    private Character disability;

    @Column(name = "aadhar_no", unique = true)
    private Long aadharNo;

    @Column(name = "mobile_no")
    private Long mobileNo;

    @Column(name = "email")
    private String email;

    @Column(name = "designation")
    private String designation;

    @ManyToOne(cascade = CascadeType.PERSIST, targetEntity = Organization.class)
    @JoinColumn(name = "organization_id", referencedColumnName = "organization_id")
    private Organization organization;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @UpdateTimestamp
    @Column(name = "updated_on")
    private Date updatedOn;

    @Column(name = "member_id", unique = true)
    private String memberId;
}
