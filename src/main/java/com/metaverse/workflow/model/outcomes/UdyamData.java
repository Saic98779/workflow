package com.metaverse.workflow.model.outcomes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "outcome_udyam_data")
public class UdyamData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "udyam_data_id")
    private Long udyamDataId;

    @Column(name = "udyam_registration_no")
    private String udyamRegistrationNo;

    @Column(name = "msme_name")
    private String nameOfMsme;

    @Column(name = "owner_name")
    private String nameOfOwner;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "gender")
    private String gender;

    @Column(name = "social_category")
    private String socialCategory;

    @Column(name = "nature_of_enterprise")
    private String natureOfEnterprise;


    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "district_name")
    private String districtName;

    @Column(name = "msme_type")
    private String typeOfMsme;

    @Column(name = "created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
