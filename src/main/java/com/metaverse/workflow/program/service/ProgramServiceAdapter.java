package com.metaverse.workflow.program.service;

import com.metaverse.workflow.activity.repository.ActivityRepository;
import com.metaverse.workflow.activity.repository.SubActivityRepository;
import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.callcenter.repository.CallCenterVerificationRepository;
import com.metaverse.workflow.common.constants.Constants;
import com.metaverse.workflow.common.constants.ProgramStatusConstants;
import com.metaverse.workflow.common.fileservice.FileSystemStorageService;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.enums.UserType;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.expenditure.repository.BulkExpenditureTransactionRepository;
import com.metaverse.workflow.expenditure.repository.ProgramExpenditureRepository;
import com.metaverse.workflow.location.repository.LocationRepository;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.notifications.dto.NotificationRequest;
import com.metaverse.workflow.notifications.repository.NotificationRepository;
import com.metaverse.workflow.notifications.service.NotificationService;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.participant.service.ParticipantResponse;
import com.metaverse.workflow.program.repository.*;
import com.metaverse.workflow.resouce.repository.ResourceRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProgramServiceAdapter implements ProgramService {

    @Autowired
    ProgramRepository programRepository;

    @Autowired
    ProgramSessionRepository programSessionRepository;
    @Autowired
    ProgramExpenditureRepository programExpenditureRepo;
    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    AgencyRepository agencyRepository;

    @Autowired
    StorageService storageService;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    ProgramSessionFileRepository programSessionFileRepository;

    @Autowired
    ProgramTypeRepository programTypeRepository;

    @Autowired
    CallCenterVerificationRepository callCenterVerificationRepository;

    @Autowired
    MediaCoverageRepository mediaCoverageRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    SubActivityRepository subActivityRepository;
   @Autowired
    BulkExpenditureTransactionRepository bulkExpRepo;
    @Autowired
    FileSystemStorageService fileSystemStorageService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    ProgramMonitoringFeedBackRepository monitoringFeedBackRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @PersistenceContext
    private EntityManager em;
    @Override
    public WorkflowResponse createProgram(ProgramRequest request) {
        Optional<Location> location = null;
        Program program = null;
        Optional<Agency> agency = agencyRepository.findById(request.getAgencyId());
        if (!agency.isPresent()) return WorkflowResponse.builder().status(400).message("Invalid Agency").build();
        if(request.getLocationId()!=null) {
            location = locationRepository.findById(request.getLocationId());
            program = programRepository.save(ProgramRequestMapper.map(request, agency.get(), location.get()));
        }
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setProgramId(program.getProgramId());
        notificationRequest.setUserType(UserType.AGENCY);
        notificationRequest.setMessage("New program scheduled: " + program.getProgramTitle());

        notificationService.saveNotification(notificationRequest);
        updateOverduePrograms();
        return WorkflowResponse.builder().status(200).message("Success").data(ProgramResponseMapper.map(program)).build();
    }



    @Override
    public WorkflowResponse createProgramSession(ProgramSessionRequest request, List<MultipartFile> files) {
        Optional<Resource> resource = resourceRepository.findById(request.getResourceId());
        if (!resource.isPresent()) return WorkflowResponse.builder().status(400).message("Invalid Resource").build();
        Optional<Program> program = programRepository.findById(request.getProgramId());
        if (!program.isPresent()) return WorkflowResponse.builder().status(400).message("Invalid Program").build();
        ProgramSession session = ProgramRequestMapper.mapSession(request, resource.get(), program.get());
        ProgramSession programSession = programSessionRepository.save(session);
        if (files != null && files.size() > 0) {
            List<String> filePaths = storageProgramFiles(files, program.get().getProgramId(), "files");
            List<ProgramSessionFile> sessionFiles = ProgramRequestMapper.mapProgramFiles(filePaths);
            sessionFiles.stream().forEach(file -> file.setProgramSession(session));
            sessionFiles = programSessionFileRepository.saveAll(sessionFiles);
            return WorkflowResponse.builder().status(200).message("Success").data(ProgramResponseMapper.mapSession(programSession, sessionFiles)).build();
        } else {
            return WorkflowResponse.builder().status(200).message("Success").data(ProgramResponseMapper.mapSession(programSession, null)).build();
        }
    }

    @Override
    public WorkflowResponse getProgramById(Long id) {
        Optional<Program> program = programRepository.findById(id);
        if (!program.isPresent()) return WorkflowResponse.builder().status(400).message("Invalid Program").build();
        ProgramResponse response = ProgramResponseMapper.mapProgram(program.get());
        return WorkflowResponse.builder().status(200).message("Success").data(response).build();
    }

    @Override
    public WorkflowResponse getProgramParticipants(Long id,Long agencyId, int page, int size) {
        Pageable pageable;
        Page<Participant> participantPage;
        List<ParticipantResponse> response;
        pageable = PageRequest.of(page, size);
        if(id == -1) {
            participantPage = participantRepository.findAll(pageable);
        }
        else if(agencyId != null) {
            participantPage = participantRepository.findByPrograms_Agency_AgencyId(agencyId, pageable);
        }
        else {
            participantPage = participantRepository.findByProgramId(id, pageable);
        }
        response = ProgramResponseMapper.mapProgramParticipants(participantPage.getContent());
        return WorkflowResponse.builder().status(200).message("Success").data(response).totalElements(participantPage.getTotalElements()).totalPages(participantPage.getTotalPages()).build();
    }

    public List<String> storageProgramFiles(List<MultipartFile> files, Long programId, String folderName) {
        List<String> uploadFilePaths = new ArrayList<>();
        files.forEach(file -> {
            String filePath = storageService.bulkExpenditureStore(file, programId, folderName);
            uploadFilePaths.add(filePath);
        });
        return uploadFilePaths;
    }

    @Override
    public WorkflowResponse getPrograms() {
        List<Program> programList = programRepository.findAll();
        List<ProgramResponse> response = programList != null ? programList.stream().map(program -> ProgramResponseMapper.map(program)).collect(Collectors.toList()) : null;
        return WorkflowResponse.builder().message("Success").status(200).data(response).build();
    }

    @Override
    @Transactional
    public WorkflowResponse updateProgram(ProgramRequest request) {
        Optional<Program> programOptional = programRepository.findById(request.getProgramId());
        if (!programOptional.isPresent())
            return WorkflowResponse.builder().status(400).message("Invalid Program").build();

        Optional<Agency> agency = agencyRepository.findById(request.getAgencyId());
        if (!agency.isPresent()) return WorkflowResponse.builder().status(400).message("Invalid Agency").build();
        Optional<Location> location = locationRepository.findById(request.getLocationId());
        if (!location.isPresent()) return WorkflowResponse.builder().status(400).message("Invalid Location").build();

        Program program = programRepository.save(ProgramRequestMapper.mapUpdate(request, agency.get(), location.get(), programOptional.get()));
        updateOverduePrograms();
        return WorkflowResponse.builder().status(200).message("Success").data(ProgramResponseMapper.map(program)).build();
    }

    @Override
    public WorkflowResponse saveProgramType(ProgramTypeRequest programTypeRequest) {
        Optional<Agency> agency = agencyRepository.findById(programTypeRequest.getAgencyId());
        if (!agency.isPresent()) return WorkflowResponse.builder().status(400).message("Agency not found").build();
        ProgramType programType = ProgramType.builder().programType(programTypeRequest.getProgramType())
                .agency(agency.get()).build();
        ProgramType response = programTypeRepository.save(programType);

        return WorkflowResponse.builder().status(200).message("Program type saved successfully").data(response).build();
    }

    @Override
    public WorkflowResponse getAllProgramTypes() {
        List<ProgramType> programTypeList = programTypeRepository.findAll();
        List<ProgramTypeResponse> typeResponses = programTypeList.stream().map(ProgramTypeResponseMapper::map).toList();
        return WorkflowResponse.builder().status(200).message("Success").data(typeResponses).build();

    }

    @Override
    public WorkflowResponse getAllProgramTypeByAgencyId(Long agencyId) {
        List<ProgramType> programTypeList = programTypeRepository.findByAgencyAgencyId(agencyId);
        if (programTypeList.isEmpty())
            return WorkflowResponse.builder().status(400).message("Program types is not found for this agency").build();
        List<ProgramTypeResponse> typeResponses = programTypeList.stream().map(ProgramTypeResponseMapper::map).toList();
        return WorkflowResponse.builder().status(200).message("Success").data(typeResponses).build();

    }

    @Override
    public WorkflowResponse getProgramParticipantAndVerifications(Long id) {
        Optional<Program> program = programRepository.findById(id);
        if (!program.isPresent()) return WorkflowResponse.builder().status(400).message("Invalid Program Id").build();
        List<ParticipantResponse> response = ProgramResponseMapper.mapProgramParticipants(program.get().getParticipants());
        List<Participant> participantList = program.get().getParticipants();
        if (participantList == null || participantList.size() == 0)
            return WorkflowResponse.builder().status(400).message("Invalid Program Id").build();
        List<CallCenterVerification> callCenterVerificationList = callCenterVerificationRepository.findByProgramId(id);
        List<ParticipantVerificationResponse> responseList = ProgramResponseMapper.mapProgramParticipantVerification(participantList, callCenterVerificationList);
        return WorkflowResponse.builder().status(200).message("Success").data(responseList).build();
    }

    @Override
    public WorkflowResponse editProgramSession(ProgramSessionRequest request, List<MultipartFile> files) {
        Optional<ProgramSession> session = programSessionRepository.findById(request.getProgramSessionId());
        if (session.isEmpty()) return WorkflowResponse.builder().status(400).message("Invalid Program Session").build();
        if (session.get().getResource().getResourceId() != request.getResourceId()) {
            Optional<Resource> resource = resourceRepository.findById(request.getResourceId());
            if (resource.isEmpty())
                return WorkflowResponse.builder().status(400).message("Invalid Resource").build();
            else
                session.get().setResource(resource.get());
        }
        if (files != null && !files.isEmpty()) {
            List<String> filePaths = storageProgramFiles(files, session.get().getProgram().getProgramId(), "files");
            List<ProgramSessionFile> sessionFiles = ProgramRequestMapper.mapProgramFiles(filePaths);
            sessionFiles.forEach(file -> file.setProgramSession(session.get()));
            sessionFiles = programSessionFileRepository.saveAll(sessionFiles);
            session.get().getProgramSessionFileList().addAll(sessionFiles);

        }
        ProgramSession response = programSessionRepository.save(ProgramRequestMapper.mapSession(request, session.get()));
        return WorkflowResponse.builder().status(200).message("Success").data(ProgramResponseMapper.mapSession(response, response.getProgramSessionFileList())).build();
    }

    @Override
    public WorkflowResponse saveSessionImages(ProgramSessionRequest request, MultipartFile image1, MultipartFile image2, MultipartFile image3, MultipartFile image4, MultipartFile image5) throws ParseException {
        Optional<ProgramSession> session = programSessionRepository.findById(request.getProgramSessionId());
        Optional<Resource> resource = resourceRepository.findById(request.getResourceId());
        if (!session.isPresent())
            return WorkflowResponse.builder().status(400).message("Invalid Program Session").build();
        ProgramSession sessionObj = session.get();
        sessionObj.setSessionStreamingUrl(request.getSessionStreamingUrl());
        if (image1 != null) {
            String filePath1 = storageService.store(image1, session.get().getProgram().getProgramId(), "photos");
            ProgramSessionFile file1 = programSessionFileRepository.save(ProgramSessionFile.builder().fileType("PHOTO").filePath(filePath1).programSessionFileId(request.getImage1()).programSession(sessionObj).build());
            sessionObj.setImage1(file1.getProgramSessionFileId());
        }
        if (image2 != null) {
            String filePath2 = storageService.store(image2, session.get().getProgram().getProgramId(), "photos");
            ProgramSessionFile file2 = programSessionFileRepository.save(ProgramSessionFile.builder().fileType("PHOTO").filePath(filePath2).programSessionFileId(request.getImage2()).programSession(sessionObj).build());
            sessionObj.setImage2(file2.getProgramSessionFileId());
        }
        if (image3 != null) {
            String filePath3 = storageService.store(image3, session.get().getProgram().getProgramId(), "photos");
            ProgramSessionFile file3 = programSessionFileRepository.save(ProgramSessionFile.builder().fileType("PHOTO").filePath(filePath3).programSessionFileId(request.getImage3()).programSession(sessionObj).build());
            sessionObj.setImage3(file3.getProgramSessionFileId());
        }
        if (image4 != null) {
            String filePath4 = storageService.store(image4, session.get().getProgram().getProgramId(), "photos");
            ProgramSessionFile file4 = programSessionFileRepository.save(ProgramSessionFile.builder().fileType("PHOTO").filePath(filePath4).programSessionFileId(request.getImage4()).programSession(sessionObj).build());
            sessionObj.setImage4(file4.getProgramSessionFileId());
        }
        if (image5 != null) {
            String filePath5 = storageService.store(image5, session.get().getProgram().getProgramId(), "photos");
            ProgramSessionFile file5 = programSessionFileRepository.save(ProgramSessionFile.builder().fileType("PHOTO").filePath(filePath5).programSessionFileId(request.getImage5()).programSession(sessionObj).build());
            sessionObj.setImage5(file5.getProgramSessionFileId());
        }
        sessionObj.setSessionDetails(request.getSessionDetails());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sessionObj.setSessionDate(sdf.parse(request.getSessionDate()));
        sessionObj.setStartTime(request.getStartTime());
        sessionObj.setEndTime(request.getEndTime());
        sessionObj.setProgramSessionId(request.getProgramSessionId());
        if(!resource.isPresent()) {
            sessionObj.setResource(resource.get());
        }
        ProgramSession response = programSessionRepository.save(sessionObj);
        return WorkflowResponse.builder().status(200).message("Success").data(ProgramResponseMapper.mapSession(response, response.getProgramSessionFileList())).build();
    }

    @Override
    public WorkflowResponse saveMediaCoverage(MediaCoverageRequest request, MultipartFile image1, MultipartFile image2, MultipartFile image3) {
        MediaCoverage coverage;
        if (request.getMediaCoverageId() != null) {
            Optional<MediaCoverage> mediaCoverage = mediaCoverageRepository.findById(request.getMediaCoverageId());
            if (!mediaCoverage.isPresent())
                return WorkflowResponse.builder().status(400).message("Invalid Program MediaCoverage").build();
            mediaCoverage.get().setMediaCoverageUrl(request.getMediaCoverageUrl());
            mediaCoverage.get().setMediaCoverageType(request.getMediaCoverageType());
            mediaCoverage.get().setDate(DateUtil.stringToDate(request.getDate(), "dd-MM-yyyy"));
            if (image1 != null) {
                String filePath1 = storageService.store(image1, mediaCoverage.get().getProgram().getProgramId(), "media");
                ProgramSessionFile file1 = programSessionFileRepository.save(ProgramSessionFile.builder().fileType("MEDIA").filePath(filePath1).programSessionFileId(request.getImage1()).build());
                mediaCoverage.get().setImage1(file1.getProgramSessionFileId());
            }
            if (image2 != null) {
                String filePath2 = storageService.store(image2, mediaCoverage.get().getProgram().getProgramId(), "media");
                ProgramSessionFile file2 = programSessionFileRepository.save(ProgramSessionFile.builder().fileType("MEDIA").filePath(filePath2).programSessionFileId(request.getImage2()).build());
                mediaCoverage.get().setImage2(file2.getProgramSessionFileId());
            }
            if (image3 != null) {
                String filePath3 = storageService.store(image3, mediaCoverage.get().getProgram().getProgramId(), "media");
                ProgramSessionFile file3 = programSessionFileRepository.save(ProgramSessionFile.builder().fileType("MEDIA").filePath(filePath3).programSessionFileId(request.getImage3()).build());
                mediaCoverage.get().setImage3(file3.getProgramSessionFileId());
            }
            coverage = mediaCoverageRepository.save(mediaCoverage.get());
        } else {
            Optional<Program> program = programRepository.findById(request.getProgramId());
            if (!program.isPresent())
                return WorkflowResponse.builder().status(400).message("Invalid Program Id").build();
            MediaCoverage mediaCoverage = MediaCoverage.builder().mediaCoverageType(request.getMediaCoverageType()).mediaCoverageUrl(request.getMediaCoverageUrl()).date(DateUtil.stringToDate(request.getDate(), "dd-MM-yyyy")).program(program.get()).build();
            if (image1 != null) {
                String filePath1 = storageService.store(image1, program.get().getProgramId(), "media");
                ProgramSessionFile file1 = programSessionFileRepository.save(ProgramSessionFile.builder().fileType("MEDIA").filePath(filePath1).build());
                mediaCoverage.setImage1(file1.getProgramSessionFileId());
            }
            if (image2 != null) {
                String filePath2 = storageService.store(image2, program.get().getProgramId(), "media");
                ProgramSessionFile file2 = programSessionFileRepository.save(ProgramSessionFile.builder().fileType("MEDIA").filePath(filePath2).build());
                mediaCoverage.setImage2(file2.getProgramSessionFileId());
            }
            if (image3 != null) {
                String filePath3 = storageService.store(image3, program.get().getProgramId(), "media");
                ProgramSessionFile file3 = programSessionFileRepository.save(ProgramSessionFile.builder().fileType("MEDIA").filePath(filePath3).build());
                mediaCoverage.setImage3(file3.getProgramSessionFileId());
            }
            coverage = mediaCoverageRepository.save(mediaCoverage);
        }
        return WorkflowResponse.builder().status(200).message("Success").data(ProgramResponseMapper.mapMediaCoverage(coverage)).build();
    }

    @Override
    public Path getProgramFile(Long fileId) {
        Optional<ProgramSessionFile> file = programSessionFileRepository.findById(fileId);
        if (file.isPresent()) {
            Path path = storageService.load(file.get().getFilePath());
            return path;
        } else
            return null;
    }

    @Override
    public List<Path> getAllProgramFile(Long programId) {
        List<ProgramSessionFile> files = programSessionFileRepository.findByProgramSession_Program_ProgramId(programId);
        return files.stream()
                .map(file -> storageService.load(file.getFilePath()))
                .collect(Collectors.toList());
    }

    @Override
    public WorkflowResponse saveCollageImages(Long programId, MultipartFile image) {
        if (image != null) {
            String filePath = storageService.store(image, programId, "Collage");
            Optional<Program> program = programRepository.findById(programId);
            ProgramSessionFile file = programSessionFileRepository.save(ProgramSessionFile.builder().fileType("COLLAGE").filePath(filePath).program(program.get()).build());

            CollageImageResponse response = CollageImageResponse.builder()
                    .programId(programId)
                    .fileId(file.getProgramSessionFileId())
                    .filePath(file.getFilePath())
                    .build();

            return WorkflowResponse.builder()
                    .status(200)
                    .message("Success")
                    .data(response)
                    .build();
        }

        return WorkflowResponse.builder()
                .status(400)
                .message("Image is required")
                .build();
    }

    @Override
    public List<ProgramFilePathInfo> getProgramFileByType(FileType fileType) {
        List<ProgramSessionFile> files = programSessionFileRepository
                .findByFileType(fileType.toString());

        return files.stream()
                .filter(file -> file.getProgram() != null)
                .map(file -> new ProgramFilePathInfo(
                        file.getProgram().getProgramId(),
                        storageService.load(file.getFilePath())
                ))
                .collect(Collectors.toList());
    }

    @Override
    public WorkflowResponse getProgramSummaryByProgramId(Long programId) throws DataException {

        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new DataException("Program data not found", "PROGRAM-DATA-NOT-FOUND", 400));

        return WorkflowResponse.builder().status(200).message("Success").data(ProgramSummeryMapper.map(program)).build();
    }

    @Override
    public String deleteProgramSession(Long sessionId) {
        Optional<ProgramSession> programSession = programSessionRepository.findById(sessionId);
        programSessionRepository.delete(programSession.get());
        List<ProgramSessionFile> sessionFiles = programSessionFileRepository.findByProgramSessionId(sessionId);
        programSessionFileRepository.deleteAll(sessionFiles);
        fileSystemStorageService.deleteAll(sessionFiles.stream().map(ProgramSessionFile::getFilePath).toList());
        return "Deleted Session Successfully";
    }

    @Override
    public WorkflowResponse getProgramParticipantsDropDown(Long id) {
        Optional<Program> program = programRepository.findById(id);
        if (!program.isPresent()) return WorkflowResponse.builder().status(400).message("Invalid Program Id").build();
        List<ParticipantResponse> response = ProgramResponseMapper.mapProgramParticipants(program.get().getParticipants());
        return WorkflowResponse.builder().status(200).message("Success").data(response).build();
    }


    public WorkflowResponse saveFeedback(ProgramMonitoringFeedBackRequest request) {
        if (monitoringFeedBackRepository.existsByProgramId(request.getProgramId())) {
            return WorkflowResponse.builder().status(400)
                    .message("Feedback already exists for Program ID: " + request.getProgramId())
                    .build();
        }
        ProgramMonitoringFeedBack monitoringFeedBack = ProgramMonitoringFeedBackMapper.mapRequest(request);
        ProgramMonitoringFeedBack savedFeedBack = monitoringFeedBackRepository.save(monitoringFeedBack);
        return WorkflowResponse.builder().status(200).message("Success")
                .data(ProgramMonitoringFeedBackMapper.mapResponse(monitoringFeedBack)).build();
    }

    @Override
    public WorkflowResponse updateFeedback(Long monitorId, ProgramMonitoringFeedBackRequest request) throws DataException {
        ProgramMonitoringFeedBack entity = monitoringFeedBackRepository.findById(monitorId)
                .orElseThrow(() -> new DataException("Feedback not found with id: " + monitorId, "FEEDBACK_NOT_FOUND", 400));

        ProgramMonitoringFeedBackMapper.updateProgramMonitoringFeedBack(entity, request);
        ProgramMonitoringFeedBack updated = monitoringFeedBackRepository.save(entity);
        return WorkflowResponse.builder().status(200).message("FeedBack Update successfully.. ")
                .data(ProgramMonitoringFeedBackMapper.mapResponse(updated)).build();
    }

    @Override
    public WorkflowResponse getFeedBackByProgramId(Long programId) throws DataException {
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new DataException("Program data not found", "PROGRAM-DATA-NOT-FOUND", 400));
        List<ProgramMonitoringFeedBack> monitoringFeedBackList = monitoringFeedBackRepository.findByProgramId(programId);

        return WorkflowResponse.builder().status(200).message("Success")
                .data(monitoringFeedBackList.stream().map(ProgramMonitoringFeedBackMapper::mapResponse)).build();
    }

    @Override
    public WorkflowResponse getFeedBackById(Long feedBackId)  {
        if (monitoringFeedBackRepository.existsById(feedBackId)) {
            Optional<ProgramMonitoringFeedBack> feedBack = monitoringFeedBackRepository.findById(feedBackId);
            return WorkflowResponse.builder().status(200).message("Success")
                    .data(ProgramMonitoringFeedBackMapper.mapResponse(feedBack.get())).build();

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

    public WorkflowResponse importProgramsFromExcel(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        try (InputStream is = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String title = getCellValue(row, 0);
                    String type = getCellValue(row, 1);
                    String agencyName = getCellValue(row, 2);
                    String activityName = getCellValue(row, 3);
                    String subActivityName = getCellValue(row, 4);
                    String startDate = getCellValue(row, 5);
                    String endDate = getCellValue(row, 6);
                    String inTime = getCellValue(row, 7);
                    String outTime = getCellValue(row, 8);
                    String kpi = getCellValue(row, 9);
                    String spocName = getCellValue(row, 10);
                    String spocContactNo = getCellValue(row, 11);
                    Location location = locationRepository.findByLocationName(Constants.YET_TO_BE_DECIDED);
                    Optional<Activity> activityOpt = activityRepository.findByActivityName(activityName);
                    Optional<SubActivity> subActivityOpt = subActivityRepository.findBySubActivityName(subActivityName);
                    if (activityOpt.isEmpty() || subActivityOpt.isEmpty()) {
                        errors.add("Row " + (i+1) + ": Invalid Activity or Sub Activity");
                        continue;
                    }

                    Optional<Agency> agencyOpt = agencyRepository.findByAgencyName(agencyName);

                    if(agencyOpt.isEmpty()) {
                        errors.add("Row " + (i+1) + ": Invalid Location or Agency");
                        continue;
                    }

                    ProgramRequest req = ProgramRequest.builder()
                            .programTitle(title)
                            .programType(type)
                            .activityId(activityOpt.get().getActivityId())
                            .subActivityId(subActivityOpt.get().getSubActivityId())
                            .startDate(startDate)
                            .endDate(endDate)
                            .startTime(inTime)
                            .endTime(outTime)
                            .kpi(kpi)
                            .spocName(spocName)
                            .spocContactNo(Long.parseLong(spocContactNo))
                            .agencyId(agencyOpt.get().getAgencyId())
                            .locationId(location.getLocationId())
                            .build();

                    WorkflowResponse singleResp = createProgram(req);
                    if (singleResp.getStatus() == 200) {
                        successCount++;
                    } else {
                        errors.add("Row " + (i+1) + ": " + singleResp.getMessage());
                    }

                } catch (Exception e) {
                    errors.add("Row " + (i+1) + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            return WorkflowResponse.builder().status(500).message("Failed to process file: " + e.getMessage()).build();
        }

        return WorkflowResponse.builder()
                .status(errors.isEmpty() ? 200 : 207)
                .message("Imported: " + successCount + " programs. Errors: " + errors.size())
                .data(errors)
                .build();
    }

    @Transactional
    @Override
    public WorkflowResponse deleteProgramAndDependencies(Long programId) {
        try {

            if (!programRepository.existsById(programId)) {
                return WorkflowResponse.builder()
                        .status(404)
                        .message("Program with ID " + programId + " not found.")
                        .build();
            }

            List<ProgramSessionFile> sessionFiles = programSessionFileRepository.findByProgramSessionId(programId);
            fileSystemStorageService.deleteAll(sessionFiles.stream().map(ProgramSessionFile::getFilePath).toList());
            programSessionFileRepository.deleteByProgramExpenditureProgramProgramId(programId);
            programSessionFileRepository.deleteByProgramProgramId(programId);
            bulkExpRepo.deleteByProgramProgramId(programId);
            mediaCoverageRepository.deleteByProgramProgramId(programId);
            programSessionRepository.deleteByProgramProgramId(programId);
            programExpenditureRepo.deleteByProgramProgramId(programId);
            notificationRepository.deleteByProgramProgramId(programId);

            em.createNativeQuery("DELETE FROM program_participant_temp WHERE program_id = :programId")
                    .setParameter("programId", programId)
                    .executeUpdate();

            em.createNativeQuery("DELETE FROM program_participant WHERE program_id = :programId")
                    .setParameter("programId", programId)
                    .executeUpdate();

            // Finally delete the program itself
            programRepository.deleteById(programId);

            return WorkflowResponse.builder()
                    .status(200)
                    .message("Program and related data deleted successfully.")
                    .build();

        } catch (Exception e) {
            return WorkflowResponse.builder()
                    .status(500)
                    .message("Failed to delete program: " + e.getMessage())
                    .build();
        }
    }



    private String getCellValue(Row row, int col) {
        Cell cell = row.getCell(col);
        if (cell == null) return "";

        if (cell.getCellType() == CellType.NUMERIC && org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
            // Format the date as a string (e.g., "dd-MM-yyyy")
            Date date = cell.getDateCellValue();
            return new SimpleDateFormat("dd-MM-yyyy").format(date);
        } else {
            cell.setCellType(CellType.STRING);
            return cell.getStringCellValue().trim();
        }
    }

    @Override
    public WorkflowResponse getProgramStatusSummery(Long agencyId) {

        List<Program> programs;

        if (agencyId == -1) {
            programs = programRepository.findAll();
        } else {
            if (!agencyRepository.existsById(agencyId)) {
                return WorkflowResponse.builder()
                        .status(400)
                        .message("Agency with ID " + agencyId + " does not exist.")
                        .build();
            }
            programs = programRepository.findByAgencyAgencyId(agencyId);
        }

        int completed = 0, inProcess = 0,completedDataPending =0 , yetToBegin =0  ,overDue = 0;
        Date today = new Date();

        for (Program program : programs) {
            Date startDate = program.getStartDate();
            Date endDate = program.getEndDate();
            String status = program.getStatus();

            if (startDate == null || endDate == null) continue;


            if (endDate.before(today)) {
                if ("Program Expenditure Updated".equalsIgnoreCase(status)) {
                    completed++;
                } else {
                    completedDataPending++;
                }
            } else if (!today.before(startDate) && !today.after(endDate)) {
                inProcess++;
            } else if (startDate.after(today)) {
                yetToBegin++;
            } else if (program.getVersion() != null){
                completedDataPending--;
                overDue++;
            }
        }

        return WorkflowResponse.builder()
                .message("success")
                .status(200)
                .data(ProgramStatusSummery.builder()
                        .programScheduled(programs.size())
                        .programInProcess(inProcess)
                        .programsCompleted(completed)
                        .programsCompletedDataPending(completedDataPending)
                        .programYetBegin(yetToBegin)
                        .programOverDue(overDue)
                        .build())
                .build();
    }

    public void updateOverduePrograms() {
        Date twoDaysAgo = java.sql.Date.valueOf(LocalDate.now().plusDays(2));
        List<Program> overdueProgramUpdate = new ArrayList<>();
        List<Program> overduePrograms = programRepository.findProgramsWithStartDateEqual(twoDaysAgo);

        for (Program program : overduePrograms) {
            System.out.println("Two days ago: " + twoDaysAgo);
            System.out.println("Found programs: " + overduePrograms.size());
            program.setOverdue(true);
            Long currentVersion = program.getVersion();
            if (currentVersion == null) {
                program.setVersion(1L);
            } else {
                program.setVersion(currentVersion + 1);
            }
            overdueProgramUpdate.add(program);
        }

        programRepository.saveAll(overdueProgramUpdate);
    }


}



