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
@ToString
@Table(name="location")
public class Location {
    @Id
    @Column(name="location_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long locationId;
    @Column(name="location_name")
    private String locationName;
    @Column(name="ownership_type")
    private String ownershipType;
    @Column(name = "venue_type")
    private String typeOfVenue;
    @Column(name = "district")
    private String district;
    @Column(name = "mandal")
    private String mandal;
    @Column(name="latitude")
    private Double latitude;
    @Column(name="longitude")
    private Double longitude;
    @Column(name="google_map_url")
    private String googleMapUrl;
    @Column(name="capacity")
    private Integer capacity;
    @Column(name="file_path")
    private String filePath;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="agency")
    @JsonIgnore
    private Agency agency;
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Program> programList;
    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

}
