package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Builder
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "counsellor_registration")
public class CounsellorRegistration {

    @Id
    @Column(name = "counsellor_registration_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long counsellorRegistrationId;

    @Column(name = "dateOf_registration")
    private Date dateOfRegistration;

    @Column(name = "nameOf_counsellor")
    private String nameOfCounsellor;

    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "socialcategory")
    private String socialCategory;

    @Column(name = "educational_qualification")
    private String educationalQualification;

    @ManyToOne
    @JoinColumn(name = "mandal_id")
    private Mandal registrationMandal;

    @Column(name = "village")
    private String village;

    @Column(name = "houseNo")
    private String houseNo;

    @Column(name = "streetname")
    private String streetName;

    @Column(name = "pincode")
    private Integer pincode;

    @Column(name = "landmark")
    private String landmark;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "designation")
    private String designation;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "contactNo")
    private Long contactNo;

    @Column(name = "altContactNo")
    private Long altContactNo;

    @Column(name = "emailId")
    private String emailId;

    @Column(name = "dateOfSelection")
    Date dateOfSelection;

    @OneToMany(mappedBy = "counsellorRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CounsellorAllotment> counsellorAllotments;

    @Column(name = "created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
