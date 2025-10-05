package com.metaverse.workflow.encryption;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Employee {

    // Personal Details
    private int id;
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
}

