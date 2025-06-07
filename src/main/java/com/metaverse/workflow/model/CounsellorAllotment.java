package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "counsellor_allotment")
public class CounsellorAllotment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "counsellor_allotment_id")
    private Long counsellorAllotmentId;

    @ManyToOne
    @JoinColumn(name = "counsellor_registration_id")
    private CounsellorRegistration counsellorRegistration;

    @ManyToOne
    @JoinColumn(name = "mandal_id")
    private Mandal allotedMandal;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "dateOfSelection")
    Date dateOfSelection;

    @Column(name = "created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;


}
