package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
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

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "date_of_visit")
    private Date dateOfVisit;

    @Column(name = "time_of_visit")
    private String timeOfVisit;

    @ManyToMany
    @JoinTable(
            name = "visit_details_non_training_resource",
            joinColumns = @JoinColumn(name = "visit_details_id"),
            inverseJoinColumns = @JoinColumn(name = "non_training_resource_id")
    )
    private List<NonTrainingResource> nonTrainingResourceList = new ArrayList<>();


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

    @Column(name ="with_in_hyderabad")
    private Boolean withInHyderabad;

    @JoinColumn(name = "subActivityId")
    @ManyToOne
    private NonTrainingSubActivity nonTrainingSubActivity;

    @Column(name="created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

}
