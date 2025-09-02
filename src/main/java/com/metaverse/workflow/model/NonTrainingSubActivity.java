package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NonTrainingSubActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sub_activity_id")
    private Long subActivityId;

    private String subActivityName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activityId")
    private NonTrainingActivity nonTrainingActivity;

    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;



}
