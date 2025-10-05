package com.metaverse.workflow.encryption;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "central_ramp_data")
@Data
public class CentralRampData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Personal Details
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;

    // Job Details
    private String designation;
    private String department;
    private LocalDate dateOfJoining;
    private String manager;

    // Salary Details
    private double salary;
    private double bonus;

    // Address (optional)
    private String address;

    // RAMP Dashboard Data
    private String statelgdCode;
    private String intervention;
    private String component;
    private String activity;
    private String year;
    private String quarter;
    private double physicalTarget;
    private double physicalAchieved;
    private double financialTarget;
    private double financialAchieved;
    private int msmesBenefittedTotal;
    private int msmesBenefittedWoman;
    private int msmesBenefittedSC;
    private int msmesBenefittedST;
    private int msmesBenefittedOBC;
}
