package com.metaverse.workflow.reports.service;

import com.metaverse.workflow.reports.dto.OutcomeTargetDTO;

import java.util.List;

public interface OutcomeTargetService {
    List<OutcomeTargetDTO> getTargetsByYear(String year, Long agencyId);
}
