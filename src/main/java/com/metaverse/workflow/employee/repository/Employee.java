package com.metaverse.workflow.employee.repository;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String department;

    private Double salary;

    private Date joiningDate;
}
