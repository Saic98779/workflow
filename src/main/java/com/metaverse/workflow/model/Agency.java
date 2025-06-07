package com.metaverse.workflow.model;

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

@Table(name="agency")
public class Agency {

    @Id
    @Column(name="agency_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long agencyId;
    @Column(name="agency_name")
    private String agencyName;
    @Column(name="mobile_no")
    private Long mobileNo;
    @Column(name="email")
    private String email;
    @Column(name="address")
    private String address;
    @OneToMany(mappedBy = "agency",cascade = CascadeType.ALL)
    private List<User> users;
    @OneToMany(mappedBy = "agency",cascade = CascadeType.ALL)
    private List<Location> locations;
    @OneToMany(mappedBy = "agency",cascade = CascadeType.ALL)
    private List<Program> programList;
    @ManyToMany(cascade = CascadeType.ALL,targetEntity = Resource.class,mappedBy = "agency")
    private List<Resource> resources;
    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
	@Override
	public String toString() {
		return "Agency [agencyId=" + agencyId + ", agencyName=" + agencyName + ", mobileNo=" + mobileNo + ", email="
				+ email + ", address=" + address + ", createdOn=" + createdOn + ", updatedOn=" + updatedOn + "]";
	}

}
