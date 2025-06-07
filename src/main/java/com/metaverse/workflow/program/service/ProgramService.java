package com.metaverse.workflow.program.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.FileType;
import com.metaverse.workflow.model.ProgramFilePathInfo;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;

public interface  ProgramService {
    WorkflowResponse createProgram(ProgramRequest request);
    WorkflowResponse createProgramSession(ProgramSessionRequest request, List<MultipartFile> files);
    WorkflowResponse getProgramById(Long id);
    WorkflowResponse getProgramParticipants(Long id, Long agencyId, int page, int size);
    public WorkflowResponse getPrograms();
    WorkflowResponse updateProgram(ProgramRequest request);
    WorkflowResponse saveProgramType(ProgramTypeRequest programTypeRequest);
    WorkflowResponse getAllProgramTypes();
    WorkflowResponse getAllProgramTypeByAgencyId(Long agencyId);
    WorkflowResponse getProgramParticipantAndVerifications(Long id);
    WorkflowResponse editProgramSession(ProgramSessionRequest request, List<MultipartFile> files);
    WorkflowResponse saveSessionImages(ProgramSessionRequest request, MultipartFile image1, MultipartFile image2, MultipartFile image3, MultipartFile image4, MultipartFile image5) throws ParseException;
    WorkflowResponse saveMediaCoverage(MediaCoverageRequest request, MultipartFile image1, MultipartFile image2, MultipartFile image3);
    Path getProgramFile(Long fileId);
    WorkflowResponse getProgramSummaryByProgramId(Long programId) throws DataException;
    String deleteProgramSession(Long sessionId);
    WorkflowResponse getProgramParticipantsDropDown(Long id);
     WorkflowResponse saveFeedback(ProgramMonitoringFeedBackRequest request);
    WorkflowResponse updateFeedback(Long monitorId, ProgramMonitoringFeedBackRequest request) throws DataException;
    WorkflowResponse getFeedBackByProgramId(Long programId) throws DataException;
    WorkflowResponse getFeedBackById(Long feedBackId);
    WorkflowResponse getProgramDetailsFroFeedBack(Long programId) throws DataException;
    List<Path> getAllProgramFile(Long programId);
    WorkflowResponse saveCollageImages(Long programId, MultipartFile image);
    WorkflowResponse importProgramsFromExcel(MultipartFile file);
    WorkflowResponse deleteProgramAndDependencies(Long programId);
    List<ProgramFilePathInfo> getProgramFileByType(FileType fileType);
    WorkflowResponse getProgramStatusSummery(Long agencyId);
}
