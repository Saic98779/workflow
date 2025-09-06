package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wehub_SDG")
public class WeHubSDG {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eeHubSDGId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id")
    private NonTrainingSubActivity nonTrainingSubActivity;

    @Column(name = "adoption_status")
    private Boolean adoptionStatus;

    @Column(name = "technology_adopted")
    private String technologyAdopted;

    @Column(name = "env_comp_cert")
    private Boolean envCompCert;

    @Column(name = "date_of_cert")
    private Date dateOfCert;

    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @Column(name = "updated_on")
    private Date updatedOn;
}
