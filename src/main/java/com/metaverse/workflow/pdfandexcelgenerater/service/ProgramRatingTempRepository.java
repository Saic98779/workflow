package com.metaverse.workflow.pdfandexcelgenerater.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramRatingTempRepository extends JpaRepository<ProgramRatingTemp, Long> {

    Optional<ProgramRatingTemp> findByProgramId(Long programId);
}