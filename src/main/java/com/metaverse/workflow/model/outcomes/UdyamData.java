package com.metaverse.workflow.model.outcomes;

import com.nimbusds.openid.connect.sdk.claims.Gender;
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
//UdyogAadharNo	EnterpriseName	PINCode	MobileNo	Gender	SocialCategory	MajorActivity	CreateDate	DISTRICT_NAME	OrganisationType	EnterpriseType
//UdyogAadharNo  EnterpriseName	PINCode	MobileNo	Gender SocialCategory	MajorActivity	CreateDate	DISTRICT_NAME	OrganisationType	EnterpriseType
//UdyogAadharNo	EnterpriseName	PINCode	MobileNo	Gender	SocialCategory	MajorActivity	CreateDate	DISTRICT_NAME	OrganisationType	EnterpriseType
    @Column(name = "udyam_registration_no")
    private String udyamRegistrationNo;

    @Column(name = "enterprise_name")
    private String enterpriseName;

    @Column(name = "pin_code")
    private Integer PINCode;

    @Column(name = "mobile_no")
    private Long mobileNo;

    @Column(name = "gender")
    private String gender;

    @Column(name = "social_category")
    private String socialCategory;

    @Column(name = "Major_activity")
    private String majorActivity;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "district_name")
    private String districtName;


    @Column(name = "organisation_type")
    private String organisationType;

    @Column(name = "enterpriseType")
    private String enterpriseType;

    @Column(name = "created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
