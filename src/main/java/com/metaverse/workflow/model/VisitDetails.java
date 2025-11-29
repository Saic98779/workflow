package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;
import java.util.List;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "visit_details")
public class VisitDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="state")
    private String state;

    @Column(name="district")
    private String district;

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

    @Column(name = "date_of_visit")
    private Date dateOfVisit;

    @Column(name = "time_of_visit")
    private String timeOfVisit;

    @OneToMany(targetEntity = Participant.class, cascade = CascadeType.ALL)
    private List<NonTrainingResource> nonTrainingResourceList;

    @Column(name="created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

}
