package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "visitor_count")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class VisitorCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "total_count", nullable = false)
    private Long totalCount;

    @Column(name = "created_timestamp", insertable = true, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdTimestamp;

    @Column(name = "last_modified", insertable = true, updatable = true)
    @UpdateTimestamp
    private LocalDateTime lastModified;

    @Version
    @Column(name = "version")
    private Integer version;
}


