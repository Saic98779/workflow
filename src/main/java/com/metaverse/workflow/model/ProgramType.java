package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@Table(name="program_type")
public class ProgramType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "program_type_id")
    private Integer programsId;

    @Column(name="program_type")
    private String programType;

    @ManyToOne
    @JoinColumn(name = "agency_id")
    @JsonIgnore
    private Agency agency;

}

