package com.metaverse.workflow.programoutcome.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.outcomes.ProgramOutcomeTable;
import net.minidev.json.parser.ParseException;

import java.util.List;
import java.util.Map;

public interface ProgramOutcomeService {

    List<ProgramOutcomeTable> getProgramOutcomeTables();

    WorkflowResponse getOutcomeDetails(Long participantId, String outcome, String type, Boolean isInfluenced);

    WorkflowResponse saveOutCome(String outcomeName, String data) throws ParseException, DataException;

    WorkflowResponse getOutcomeDetailsByName(String outcome);

    WorkflowResponse getApiForOutcomes(Long agencyId, Long outcomeId);

    WorkflowResponse getOutcomeData(String outcomeName, Long agencyId, int page, int size);

    WorkflowResponse getOutcomeDataById(String outcomeName, Long outcomeId) throws DataException;

    WorkflowResponse updateOutCome(String outcomeName, String data, Long id) throws ParseException, DataException;

}
