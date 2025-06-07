package com.metaverse.workflow.programoutcometargets.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.FinancialTarget;
import com.metaverse.workflow.model.PhysicalTarget;
import com.metaverse.workflow.model.outcomes.ProgramOutcomeTable;
import com.metaverse.workflow.programoutcome.repository.ProgramOutcomeTableRepository;
import com.metaverse.workflow.programoutcometargets.repository.FinancialRepository;
import com.metaverse.workflow.programoutcometargets.repository.PhysicalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TargetServiceAdepter implements TargetService {
    @Autowired
    private PhysicalRepository physicalRepository;
    @Autowired
    private FinancialRepository financialRepository;
    @Autowired
    private AgencyRepository agencyRepository;
    @Autowired
    private ProgramOutcomeTableRepository outcomeTableRepository;

    @Override
    public WorkflowResponse saveFinancialTarget(FinancialTargetRequest request) throws DataException {
        Agency agency = agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new DataException("Agency not found","AGENCY_NOT_FOUND",400));
        ProgramOutcomeTable outcomeTable = outcomeTableRepository.findById(request.getOutcomeId())
                .orElseThrow(() -> new DataException("Outcome table not found","OUTCOME_NOT_FOUND",400));

        FinancialTarget savedTarget = financialRepository.save(TargetRequestMapper.mapFinancialTarget(request, agency, outcomeTable));

        return WorkflowResponse.builder().status(200).message("Target saved Successfully")
                .data(TargetResponseMapper.mapFinancialTarget(savedTarget)).build();
    }

    @Override
    public WorkflowResponse updateFinancialTarget(FinancialTargetRequest request, Long financialTargetId) throws DataException {
        FinancialTarget existingTarget = financialRepository.findById(financialTargetId)
                .orElseThrow(() -> new DataException("Financial target not found","TARGET_NOT_FOUND",400));

        TargetRequestMapper.mapUpdateFinancialTarget(existingTarget, request);
        FinancialTarget updatedTarget = financialRepository.save(existingTarget);

        return WorkflowResponse.builder().status(200).message("Target updated Successfully")
                .data(TargetResponseMapper.mapFinancialTarget(updatedTarget)).build();
    }



    @Override
    public WorkflowResponse getFinancialTargetsById(Long financialTargetId) throws DataException {
        FinancialTarget target = financialRepository.findById(financialTargetId)
                .orElseThrow(() -> new DataException("Financial target not found","TARGET_NOT_FOUND",400));
        return WorkflowResponse.builder().status(200).message("Target updated Successfully")
                .data(TargetResponseMapper.mapFinancialTarget(target))
                .build(); }

    @Override
    public WorkflowResponse getFinancialTargetsByAgencyId(Long agencyId) throws DataException {
        List<FinancialTarget> targets;
        if(agencyId==-1) {
            targets = financialRepository.findAll();
            if(targets.isEmpty()) return WorkflowResponse.builder().status(400).message("Target Data not found").build();
        }
        else if(!agencyRepository.existsById(agencyId))return WorkflowResponse.builder().status(400).message("agency not found").build();
        else{
             targets = financialRepository.findByAgencyAgencyId(agencyId);
            if (targets.isEmpty()) return WorkflowResponse.builder().status(400).message("Targets  not assigned for this agency").build();
        }
        return WorkflowResponse.builder().status(200).message("Success")
                .data(targets.stream().map(TargetResponseMapper::mapFinancialTarget).toList())
                .build();
    }

    @Override
    public WorkflowResponse deleteFinancialTarget(Long financialTargetId) throws DataException {
         if(financialRepository.existsById(financialTargetId)) {
            financialRepository.deleteById(financialTargetId);
            return WorkflowResponse.builder().status(200).message("Financial Target Deleted Successfully").build();
        }else {
            return WorkflowResponse.builder().status(400).message("Financial Target Not Found in This ID - "+financialTargetId).build();
        }

    }

    @Override
    public WorkflowResponse savePhysicalTarget(PhysicalTargetRequest request) throws DataException {
        Agency agency = agencyRepository.findById(request.getAgencyId())
                .orElseThrow(() -> new DataException("Agency not found","",400));

        ProgramOutcomeTable outcomeTable = outcomeTableRepository.findById(request.getOutcomeId())
                .orElseThrow(() -> new DataException("Outcome table not found","",400));

        List<PhysicalTarget> existingTargets = physicalRepository
                .findByProgramOutcomeTableAndFinancialYearAndAgencyAgencyId(outcomeTable, request.getFinancialYear(),agency.getAgencyId());

        if (!existingTargets.isEmpty()) {
            throw new DataException("Target for the given outcome and financial year already exists", "TARGET-EXISTS", 409);
        }

        PhysicalTarget savedTarget = physicalRepository.save(TargetRequestMapper.mapPhysicalTarget(request, agency, outcomeTable));

        return WorkflowResponse.builder().status(200).message("Target saved Successfully")
                .data(TargetResponseMapper.mapPhysicalTarget(savedTarget)).build();
    }

    @Override
    public WorkflowResponse updatePhysicalTarget(PhysicalTargetRequest request, Long physicalTargetId) throws DataException {
        PhysicalTarget existingTarget = physicalRepository.findById(physicalTargetId)
                .orElseThrow(() -> new DataException("Physical target not exist with this id"+physicalTargetId,"PHYSICAL_TARGET_NOT_FOUND",400));

        TargetRequestMapper.mapUpdatePhysicalTarget(existingTarget, request);
        PhysicalTarget updatedTarget = physicalRepository.save(existingTarget);

        return WorkflowResponse.builder().status(200).message("Target updated Successfully")
                .data(TargetResponseMapper.mapPhysicalTarget(updatedTarget)).build();
    }

    @Override
    public WorkflowResponse getPhysicalTargetsById(Long physicalTargetId) throws DataException {
        PhysicalTarget target = physicalRepository.findById(physicalTargetId)
                .orElseThrow(() -> new DataException("Physical target not exist with this id"+physicalTargetId,"PHYSICAL_TARGET_NOT_FOUND",400));

        return WorkflowResponse.builder().status(200).message("Target updated Successfully")
                .data(TargetResponseMapper.mapPhysicalTarget(target))
                .build();
    }

    @Override
    public WorkflowResponse getPhysicalTargetsByAgencyId(Long agencyId) throws DataException {
        List<PhysicalTarget> targets;
        if(agencyId==-1) {
            targets = physicalRepository.findAll();
            if(targets.isEmpty()) return WorkflowResponse.builder().status(400).message("Target Data not found").build();
        }
        else if(!agencyRepository.existsById(agencyId))return WorkflowResponse.builder().status(400).message("agency not found").build();
        else{
            targets = physicalRepository.findByAgencyAgencyId(agencyId);
            if (targets.isEmpty()) return WorkflowResponse.builder().status(400).message("Targets  not assigned for this agency").build();
        }

        Map<String, OutcomeGroup> groupedData = new HashMap<>();
        Map<String, Set<String>> yearTracker = new HashMap<>();
        Set<String> financialYearHeaders = new HashSet<>();
        for (PhysicalTarget target : targets) {
            TargetResponse response = TargetResponseMapper.mapPhysicalTargetResponse(target);
            String outcomeName = response.getOutcomeTableName();
            String year = response.getFinancialYear();
            long total = response.getTotal() != null ? response.getTotal() : 0L;

            OutcomeGroup group = groupedData.computeIfAbsent(outcomeName, k -> {
                OutcomeGroup og = new OutcomeGroup();
                og.setOutcomeTableName(k);
                og.setGrandTotal(0L);
                return og;
            });

            Set<String> trackedYears = yearTracker.computeIfAbsent(outcomeName, k -> new HashSet<>());
            financialYearHeaders.add(year);
            if (trackedYears.add(year)) {
                group.getFinancialYear().add(response);
                group.setGrandTotal(group.getGrandTotal() + total);
                group.setFinancialYearHeaders(financialYearHeaders);
            }
        }

        return WorkflowResponse.builder().status(200).message("Success")
                .data(groupedData)
                .build();
    }

    @Override
    public WorkflowResponse deletePhysicalTarget(Long physicalTargetId) throws DataException {
        if(physicalRepository.existsById(physicalTargetId)) {
            physicalRepository.deleteById(physicalTargetId);
            return WorkflowResponse.builder().status(200).message("Physical Target Deleted Successfully").build();
        }else {
            return WorkflowResponse.builder().status(400).message("Physical Target Not Found in This ID"+physicalTargetId).build();
        }
    }
}
