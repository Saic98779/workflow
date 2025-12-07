package com.metaverse.workflow.programstatus.entity;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "program_status")
public class ProgramStatus  extends BaseEntity {
    private String programId;
    private String status;
}
