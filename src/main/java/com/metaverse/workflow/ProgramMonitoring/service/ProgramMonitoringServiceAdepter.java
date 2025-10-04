package com.metaverse.workflow.ProgramMonitoring.service;

import com.metaverse.workflow.ProgramMonitoring.repository.ProgramMonitoringRepo;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.CommonUtil;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.ProgramMonitoring;
import com.metaverse.workflow.model.User;
import com.metaverse.workflow.program.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ProgramMonitoringServiceAdepter implements ProgramMonitoringService{
    private final ProgramMonitoringRepo programMonitoringRepo;
    private final ProgramRepository programRepository;
    private final LoginRepository loginRepository;

    public WorkflowResponse saveFeedback(ProgramMonitoringRequest request) throws DataException {
        User user=loginRepository.findById(request.getUserId())
                .orElseThrow(()->new DataException("user not found with this id "+request.getUserId(),"USER_NOT_FOUND",400));
        ProgramMonitoring monitoring = ProgramMonitoringMapper.mapRequest(request,user);
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
    public WorkflowResponse getFeedBackByProgramIdDropDown(Long programId) throws DataException {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new DataException("Program data not found", "PROGRAM-DATA-NOT-FOUND", 400));
        List<ProgramMonitoring> monitoringList = programMonitoringRepo.findByProgramId(programId);

        return WorkflowResponse.builder().status(200).message("Success")
                .data(monitoringList.stream().map(report->ProgramMonitoringDetails.builder()
                        .programMonitoringId(report.getProgramMonitoringId())
                        .programName(CommonUtil.programMap.get(report.getProgramId()))
                        .userId(report.getUser().getUserId())
                        .userName(report.getUser().getFirstName()+" "+report.getUser().getLastName())
                        .monitoringDate(DateUtil.dateToString(report.getMoniteringDate(),"dd-MM-yyyy"))
                        .build()
                )).build();
    }
}
