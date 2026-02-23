package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "ramp_registration")
public class RampRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rampRegistrationId;

    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String email;
    private String phoneNo;
    private String aadhaarNo;
    private String udhyamRegistrationNo;
    private String socialCategory;
    private Boolean minorities;
    private Boolean disability;
    private String designation;
    private String organization;
    private String sector;
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", referencedColumnName = "program_id", unique = true)
    private Program program;
}
