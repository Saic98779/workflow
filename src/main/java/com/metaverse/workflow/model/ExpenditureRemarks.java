package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "expenditure_remarks")
public class ExpenditureRemarks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String remark;

    private LocalDate remarkDate;

    private LocalTime remarkTime;

    @ManyToOne
    @JoinColumn(name = "expenditure")
    @JsonIgnore
    private ProgramExpenditure expenditure;

    @PrePersist
    public void prePersist() {
        LocalDate nowDate = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        this.remarkDate = nowDate;
        this.remarkTime = nowTime;
    }

}
