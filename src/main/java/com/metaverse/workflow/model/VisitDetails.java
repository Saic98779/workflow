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

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "date_of_visit")
    private Date dateOfVisit;

    @Column(name = "time_of_visit")
    private String timeOfVisit;

    @OneToMany
    @JoinColumn(name = "visit_details_id")
    private List<NonTrainingResource> nonTrainingResourceList;


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
