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
@Table(name="media_coverage")
public class MediaCoverage {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="media_coverage_id")
    private Long mediaCoverageId;
    @Column(name="media_coverage_type")
    private String mediaCoverageType;
    @Column(name="image1")
    private Long image1;
    @Column(name="image2")
    private Long image2;
    @Column(name="image3")
    private Long image3;
    @Column(name="media_coverage_url")
    private String mediaCoverageUrl;
    @Column(name="date")
    private Date date;
    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;
}
