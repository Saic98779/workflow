package com.metaverse.workflow.model.outcomes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "program_outcome_tables")
public class ProgramOutcomeTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "outcome_table_id")
    private Integer outcomeTableId;
    @Column(name = "outcome_table_name")
    private String outcomeTableName;
    @Column(name = "outcome_table_display_name")
    private String outcomeTableDisplayName;
    @Column(name = "created_on", insertable = true, updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name = "updated_on", insertable = false, updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

}
