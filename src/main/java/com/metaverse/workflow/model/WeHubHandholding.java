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
@Table(name = "wehub_handholding")
public class WeHubHandholding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long handholdingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "status_product_diversification")
    private Boolean statusProductDiversification;

    @Column(name = "date_support_received")
    private Date dateSupportReceived;

    @Column(name = "ipr_name")
    private String iprName;

    @Column(name = "ipr_filing_date")
    private Date iprFilingDate;

    @Column(name = "status_trademark_filing")
    private Boolean statusTrademarkFiling;

    @Column(name = "trademark_filing_date")
    private Date trademarkFilingDate;

    @Column(name = "status_inventory_management")
    private Boolean statusInventoryManagement;

    @Column(name = "inventory_adoption_date")
    private Date inventoryAdoptionDate;

    @Column(name = "lean_name")
    private String leanName;

    @Column(name = "lean_adoption_date")
    private Date leanAdoptionDate;

    @Column(name = "new_machinery_adoption")
    private Boolean newMachineryAdoption;

    @Column(name = "establishment_date")
    private Date establishmentDate;

    @Column(name = "status_technology_redesign")
    private Boolean statusTechnologyRedesign;

    @Column(name = "technology_redesign_date")
    private Date technologyRedesignDate;

    @Column(name = "status_digital_solution")
    private Boolean statusDigitalSolution;

    @Column(name = "digital_solution_date")
    private Date digitalSolutionDate;

    @Column(name = "innovative_process_name")
    private String innovativeProcessName;

    @Column(name = "innovative_process_date")
    private Date innovativeProcessDate;

    @Column(name = "skill_training_name")
    private String skillTrainingName;

    @Column(name = "skill_training_person")
    private String skillTrainingPerson;

    @Column(name = "skill_training_date")
    private Date skillTrainingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id")
    private NonTrainingSubActivity nonTrainingSubActivity;

    @Column(name = "created_on", updatable = false)
    private Date createdOn;

    @Column(name = "updated_on")
    private Date updatedOn;

}
