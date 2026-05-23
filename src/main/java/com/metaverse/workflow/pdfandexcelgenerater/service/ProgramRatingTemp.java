package com.metaverse.workflow.pdfandexcelgenerater.service;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "program_rating_temp")
@Data
public class ProgramRatingTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "agency_id")
    private Long agencyId;

    @Column(name = "program_id")
    private Long programId;

    @Column(name = "monitoring_rating")
    private Double monitoringRating;
}