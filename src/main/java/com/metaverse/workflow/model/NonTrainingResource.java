package com.metaverse.workflow.model;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "non_training_resources")
public class NonTrainingResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id")
    private Long resourceId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "designation")
    private String designation;

    @Column(name = "relevant_experience_years")
    private Double relevantExperience;

    @Column(name = "educational_qualifications")
    private String educationalQualifications;

    @Column(name = "date_of_joining")
    private Date dateOfJoining;

    @Column(name = "monthly_salary")
    private Double monthlySal;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "ifsc_code", length = 20)
    private String ifscCode;

    @Column(name = "account_no", unique = true, length = 30)
    private String accountNo;

    @OneToMany(mappedBy = "nonTrainingResource", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NonTrainingResourceExpenditure> nonTrainingResourceExpenditures = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private NonTrainingActivity nonTrainingActivity;
}
