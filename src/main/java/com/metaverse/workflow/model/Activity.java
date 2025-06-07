package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name="activity")
public class Activity {
    @Id
    @Column(name="activity_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long activityId;
    @Column(name="activity_name")
    private String activityName;
    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "activity_sub_activity",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "sub_activity_id")
    )  @JsonManagedReference
    private List<SubActivity> subActivities;


}
