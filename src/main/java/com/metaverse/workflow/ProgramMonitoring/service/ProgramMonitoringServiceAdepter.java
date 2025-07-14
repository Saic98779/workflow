package com.metaverse.workflow.ProgramMonitoring.service;

import com.metaverse.workflow.ProgramMonitoring.repository.ProgramMonitoringRepo;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.ProgramMonitoring;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.service.ProgramDetailsFroFeedBack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ProgramMonitoringServiceAdepter implements ProgramMonitoringService{
    private final ProgramMonitoringRepo programMonitoringRepo;
    private final ProgramRepository programRepository;

    public WorkflowResponse saveFeedback(ProgramMonitoringRequest request) {
        if (programMonitoringRepo.existsByProgramId(request.getProgramId())) {
            return WorkflowResponse.builder().status(400)
                    .message("Feedback already exists for Program ID: " + request.getProgramId())
                    .build();
        }
        ProgramMonitoring monitoring = ProgramMonitoringMapper.mapRequest(request);
        ProgramMonitoring savedFeedBack = programMonitoringRepo.save(monitoring);
        return WorkflowResponse.builder().status(200).message("Success")
                .data(ProgramMonitoringMapper.mapResponse(monitoring)).build();
    }

    @Override
    public WorkflowResponse updateFeedback(Long monitorId, ProgramMonitoringRequest request) throws DataException {
        ProgramMonitoring entity = programMonitoringRepo.findById(monitorId)
                .orElseThrow(() -> new DataException("Feedback not found with id: " + monitorId, "FEEDBACK_NOT_FOUND", 400));

        ProgramMonitoringMapper.updateProgramMonitoring(entity, request);
        ProgramMonitoring updated = programMonitoringRepo.save(entity);
        return WorkflowResponse.builder().status(200).message("FeedBack Update successfully.. ")
                .data(ProgramMonitoringMapper.mapResponse(updated)).build();
    }

    @Override
    public WorkflowResponse getFeedBackByProgramId(Long programId) throws DataException {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new DataException("Program data not found", "PROGRAM-DATA-NOT-FOUND", 400));
        List<ProgramMonitoring> monitoringList = programMonitoringRepo.findByProgramId(programId);

        return WorkflowResponse.builder().status(200).message("Success")
                .data(monitoringList.stream().map(ProgramMonitoringMapper::mapResponse)).build();
    }

    @Override
    public WorkflowResponse getFeedBackById(Long feedBackId) {
        if (programMonitoringRepo.existsById(feedBackId)) {
            Optional<ProgramMonitoring> feedBack = programMonitoringRepo.findById(feedBackId);
            return WorkflowResponse.builder().status(200).message("Success")
                    .data(ProgramMonitoringMapper.mapResponse(feedBack.get())).build();

        }
        return WorkflowResponse.builder()
                .status(400)
                .message("MonitorFeedback not found for the given ID: " + feedBackId)
                .build();


    }

    @Override
    public WorkflowResponse getProgramDetailsFroFeedBack(Long programId) throws DataException {

        Program program = programRepository.findById(programId).orElseThrow(() -> new DataException("Program data not found", "PROGRAM-DATA-NOT-FOUND", 400));
        ProgramDetailsFroFeedBack programDetailsFroFeedBack = ProgramDetailsFroFeedBack.builder()
                .state("Telangana")
                .district(program.getLocation().getDistrict())
                .dateOfMonitoring(DateUtil.dateToString(program.getStartDate(), "dd-MM-yyyy"))
                .programName(program.getProgramTitle())
                .programType(program.getProgramType())
                .agencyName(program.getAgency() != null ? program.getAgency().getAgencyName() : null)
                .hostingAgencyName(program.getAgency() != null ? program.getAgency().getAgencyName() : null)
                .inTime(program.getStartTime())
                .outTime(program.getEndTime())
                .spocName(program.getSpocName())
                .spocContact(program.getSpocContactNo())
                .venueName(program.getLocation().getLocationName()).build();
        return WorkflowResponse.builder().message("Success").status(200)
                .data(programDetailsFroFeedBack)
                .build();
    }
}
