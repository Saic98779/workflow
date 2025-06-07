package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name="resource")
public class Resource {
    @Id
    @Column(name="resource_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long resourceId;
    @Column(name="name")
    private String name;
    @Column(name="gender")
    private Character gender;
    
    @Column(name="mobile_no")
    private Long mobileNo;
    @Column(name="organization_name")
    private String organizationName;
    @Column(name="email")
    private String email;
    @Column(name="designation")
    private String designation;
    @Column(name="qualification")
    private String qualification;
    @Column(name="specialization")
    private String specialization;
  
    @Column(name="brief_description")
    private String briefDescription;

    @Column(name="is_vip")
    private Boolean isVIP;
    
    @ManyToMany
    @JoinTable(name = "agency_resource",
    joinColumns = @JoinColumn(name="resource_id",referencedColumnName = "resource_id"),
    inverseJoinColumns = @JoinColumn(name="agency_id",referencedColumnName = "agency_id"))
    @JsonIgnore
    private List<Agency> agency;
    @OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProgramSession> ProgramSessionList;
    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
