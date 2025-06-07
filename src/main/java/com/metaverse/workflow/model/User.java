package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name="user")
public class User {

    @Id
    @Column(name="user_id")
    private String userId;
    @Column(name="email")
    private String email;
    @Column(name="password")
    private String password;
    @Column(name="user_role")
    private String userRole;
    @Column(name="attempts")
    private Integer attempts;
    @Column(name="status")
    private String status;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="gender")
    private String gender;
    @Column(name="mobile_no")
    private Long mobileNo;
    @Column(name="address")
    private String address;
    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
    @ManyToOne
    @JoinColumn(name = "agency_id")
    private Agency agency;

}
