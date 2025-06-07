package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="sector")
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="sector_id")
    private Integer sectorId;

    @Column(name="sector_name")
    private String sectorName;

    @ManyToMany(mappedBy = "sectors")
    @JsonIgnore
    private List<Organization> organizations;

    @Column(name="created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

}
