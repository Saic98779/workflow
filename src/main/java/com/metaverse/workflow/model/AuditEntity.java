package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditEntity {

    @CreatedDate
    @Column(name = "created_timestamp", updatable = false)
    protected LocalDateTime createdTimestamp;

    @LastModifiedDate
    @Column(name = "last_modified")
    protected LocalDateTime lastModified;

    @Version
    protected Integer version;
}
