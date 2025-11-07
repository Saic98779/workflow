package com.metaverse.workflow.model;

import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = {"participants", "sectors"})
@Table(
        name = "organization",
        indexes = {
                @Index(name = "idx_org_name", columnList = "organization_name"),
                @Index(name = "idx_org_type", columnList = "organization_type"),
                @Index(name = "idx_org_category", columnList = "organization_category"),
                @Index(name = "idx_org_state", columnList = "state_id"),
                @Index(name = "idx_org_dist", columnList = "dist_id"),
                @Index(name = "idx_org_mandal", columnList = "mandal"),
                @Index(name = "idx_org_email", columnList = "email"),
                @Index(name = "idx_org_udyam_no", columnList = "udyam_registration_no"),
                @Index(name = "idx_org_owner_name", columnList = "owner_name"),
                @Index(name = "idx_org_contact_no", columnList = "contact_no")
        }
)
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="organization_id")
    private Long organizationId;

    @Column(name="organization_name")
    private String organizationName;

    @Column(name="organization_category")
    private String organizationCategory;

    @Column(name="organization_type")
    private String organizationType;

    @Column(name="udyam_registration_no")
    private String udyamregistrationNo;

    @Column(name="date_of_registration")
    private Date dateOfRegistration;

    @Column(name="startup_certificate_no")
    private String startupCertificateNo;

    @Column(name="nature_of_startup")
    private String natureOfStartup;

    @Column(name="areas_of_working")
    private String areasOfWorking;

    @Column(name="incorporation_date")
    private Date incorporationDate;

    @Column(name="date_of_issue")
    private String dateOfIssue;

    @Column(name="valid_upto")
    private String validUpto;

    @Column(name="state_id")
    private String stateId;

    @Column(name="dist_id")
    private String distId;

    @Column(name="mandal")
    private String mandal;

    @Column(name="town")
    private String town;

    @Column(name="street_no")
    private String streetNo;

    @Column(name="house_no")
    private String houseNo;

    @Column(name="latitude")
    private Double latitude;

    @Column(name="longitude")
    private Double longitude;

    @Column(name="contact_no")
    private Long contactNo;

    @Column(name="email")
    private String email;

    @Column(name="website")
    private String website;

    @Column(name="owner_name")
    private String ownerName;

    @Column(name="owner_contact_no")
    private Long ownerContactNo;

    @Column(name="owner_email")
    private String ownerEmail;

    @Column(name="owner_address")
    private String ownerAddress;

    @Column(name="name_of_the_SHG")
    private String nameOfTheSHG;

    @Column(name="name_of_the_VO")
    private String nameOfTheVO;

    @Column(name="grama_panchayat")
    private String gramaPanchayat;

    @OneToMany(targetEntity = Participant.class, cascade = CascadeType.ALL, mappedBy = "organization")
    private List<Participant> participants;

    @Column(name="created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

    @ManyToMany
    @JoinTable(
            name = "organization_sector",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "sector_id")
    )
    private List<Sector> sectors;
}
