package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name="sub_activity")
public class SubActivity {
    @Id
    @Column(name="sub_activity_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long subActivityId;
    @Column(name="sub_activity_name")
    private String subActivityName;
    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

    @JsonBackReference
    @ManyToMany(mappedBy = "subActivities")
    private List<Activity> activities;
}
