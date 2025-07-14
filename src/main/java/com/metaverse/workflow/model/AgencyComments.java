package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "agency_comments")
public class AgencyComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private String remarks;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy HH:mm:ss")
    private LocalDateTime remarkTimestamp;

    @ManyToOne
    @JoinColumn(name = "expenditure")
    @JsonIgnore
    private ProgramExpenditure expenditure;

    @ManyToOne
    @JoinColumn(name = "bulkExpenditureTransaction", referencedColumnName = "bulk_expenditure_transaction_id")
    @JsonIgnore
    private BulkExpenditureTransaction bulkExpenditureTransaction;

    @Transient
    public String getFormattedRemark() {
        if (remarkTimestamp == null || remarks == null) return "";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
        return remarkTimestamp.format(formatter) + ": " + remarks;
    }

    @PrePersist
    public void prePersist() {
        this.remarkTimestamp = LocalDateTime.now();
    }

}

