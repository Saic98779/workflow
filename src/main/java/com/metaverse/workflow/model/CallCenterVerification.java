package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="call_center_verification", uniqueConstraints = { @UniqueConstraint(columnNames = { "program_id", "participant_id" }) })
public class CallCenterVerification {
    /*@EmbeddedId
    private CallCenterVerificationId callCenterVerificationId;*/

    @Id
    @Column(name="call_center_verification_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long callCenterVerificationId;

    @Column(name = "program_id", nullable = false)
    private Long programId;

    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User verifiedBy;

    @Column(name="verification_date")
    private Date verificationDate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "call_center_verification_question_answers",
            joinColumns = @JoinColumn(name = "call_center_verification_id"),
            inverseJoinColumns = @JoinColumn(name = "question_answers_id")
    )
    @JsonManagedReference
    private List<QuestionAnswers> questionAnswers;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cc_verification_status_id", referencedColumnName = "cc_verification_status_id")
    private CallCenterVerificationStatus ccVerificationStatus;

    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

}
