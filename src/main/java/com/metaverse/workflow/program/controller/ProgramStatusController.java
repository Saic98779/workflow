package com.metaverse.workflow.program.controller;

import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.constants.ProgramStatusConstants;
import com.metaverse.workflow.common.enums.NotificationType;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.email.EmailNotificationController;
import com.metaverse.workflow.email.EmailRequest;
import com.metaverse.workflow.email.entity.EmailConfiguration;
import com.metaverse.workflow.email.repository.EmailConfigurationRepository;
import com.metaverse.workflow.email.util.EmailUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.program.repository.ProgramRepository;
import com.metaverse.workflow.program.service.ProgramRequestDto;
import com.metaverse.workflow.program.service.ProgramResponse;
import com.metaverse.workflow.program.service.ProgramResponseMapper;
import com.metaverse.workflow.program.service.ProgramService;
import com.metaverse.workflow.notifications.dto.GlobalNotificationRequest;
import com.metaverse.workflow.notifications.service.NotificationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/programs/status")
public class ProgramStatusController {

    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private ProgramService programService;
    @Autowired
    private ActivityLogService logService;

    // Notification/email services
    @Autowired
    private NotificationServiceImpl notificationService;

    @Autowired
    private EmailNotificationController emailNotificationController;

    @Autowired
    EmailConfigurationRepository emailConfigurationRepository;


    @PostMapping("/{programId}")
    public WorkflowResponse updateProgramStatus(Principal principal,
                                                @PathVariable Long programId,
                                                @RequestParam String status,
                                                HttpServletRequest servletRequest) {

        Optional<Program> programOptional = programRepository.findById(programId);

        if (programOptional.isEmpty()) {
            return WorkflowResponse.builder().message("Program not found").status(HttpStatus.BAD_REQUEST.value()).data(programId).build();
        }
        Program program = programOptional.get();
        if (isValidStatus(status)) {
            return WorkflowResponse.builder().message("Invalid status value." + status).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).data(programId).build();
        }
        program.setStatus(status);
        programRepository.save(program);

        EmailConfiguration emailConfiguration = emailConfigurationRepository.findByAgency_AgencyId(program.getAgency().getAgencyId());

        // send notification (async)
        try {
            GlobalNotificationRequest req = GlobalNotificationRequest.builder()
                    .userId("-1")
                    .agencyId(program.getAgency() != null ? program.getAgency().getAgencyId() : -1)
                    .programId(programId)
                    .notificationType(NotificationType.RESCHEDULE_PROGRAMS)
                    .message("Program " + programId + " status changed to " + status)
                    .sentBy("ADMIN")
                    .isRead(false)
                    .build();

            notificationService.saveNotification(req);
        } catch (Exception e) {
            // Don't block success - log and continue
            logService.logs(principal != null ? principal.getName() : "system", "ERROR", "Failed to queue notification: " + e.getMessage(), "Notification", servletRequest.getRequestURI());
        }

        if(status.equalsIgnoreCase(ProgramStatusConstants.PROGRAM_SCHEDULED) || status.equalsIgnoreCase(ProgramStatusConstants.PROGRAM_EXPENDITURE_UPDATED)
        || status.equalsIgnoreCase(ProgramStatusConstants.PROGRAM_EXPENDITURE_APPROVED)) {
            try {
                String actionRequired =
                        status.equalsIgnoreCase(ProgramStatusConstants.PROGRAM_SCHEDULED)
                                ? "No action required"
                                : "Please review the updated expenditure details";

                EmailRequest emailRequest = EmailUtil.getEmailRequest(
                        status,
                        program,
                        actionRequired, emailConfiguration
                );

                emailNotificationController.sendEmail(emailRequest);
            }
            catch (Exception e) {
                // Don't block success - log and continue
                logService.logs(principal != null ? principal.getName() : "system", "ERROR", "Failed to send email: " + e.getMessage(), "Email", servletRequest.getRequestURI());
            }
        }


        logService.logs(principal != null ? principal.getName() : "system","UPDATE","Program status updated successfully with status " + status,"program", servletRequest.getRequestURI());
        return WorkflowResponse.builder().message("Program status updated successfully to: " + status).status(HttpStatus.OK.value()).data(programId).build();
    }

    private boolean isValidStatus(String status) {
        return !status.equals(ProgramStatusConstants.PROGRAM_SCHEDULED) &&
                !status.equals(ProgramStatusConstants.SESSIONS_CREATED) &&
                !status.equals(ProgramStatusConstants.PARTICIPANTS_ADDED) &&
                !status.equals(ProgramStatusConstants.ATTENDANCE_MARKED) &&
                !status.equals(ProgramStatusConstants.PROGRAM_EXECUTION_UPDATED) &&
                !status.equals(ProgramStatusConstants.PROGRAM_EXECUTION) &&
                !status.equals(ProgramStatusConstants.PROGRAM_EXPENDITURE_UPDATED) &&
                !status.equals(ProgramStatusConstants.PROGRAM_EXPENDITURE_APPROVED) &&
                !status.equals(ProgramStatusConstants.COLLAGE_ADDED);
    }

    @GetMapping("/{agencyId}")
    public WorkflowResponse getProgramsByStatus(@PathVariable Long agencyId,
                                                @RequestParam String status) {
        List<Program> programs = new ArrayList<>();
        if (isValidStatus(status)) {
            return WorkflowResponse.builder().message("Invalid status value." + status).status(HttpStatus.INTERNAL_SERVER_ERROR.value()).data(status).build();
        } else {
            if (ProgramStatusConstants.PROGRAM_EXECUTION.equalsIgnoreCase(status)) {
                List<String> statuses = Arrays.asList(
                        ProgramStatusConstants.SESSIONS_CREATED,
                        ProgramStatusConstants.PARTICIPANTS_ADDED,
                        ProgramStatusConstants.ATTENDANCE_MARKED
                );
                programs = programRepository.findByAgencyAgencyIdAndStatusIn(agencyId, statuses);
            } else {
                programs = programRepository.findByAgencyAgencyIdAndStatus(agencyId, status);
            }
            List<ProgramResponse> response = programs != null ? programs.stream().map(ProgramResponseMapper::map).collect(Collectors.toList()) : null;
            return WorkflowResponse.builder().message("Success").status(200).data(response).build();
        }
    }

    @GetMapping("/summary/{agencyId}")
    public WorkflowResponse getProgramsStatusSummery(@PathVariable Long agencyId,
                                                     @RequestParam(required = false) String fromDate,
                                                     @RequestParam(required = false) String toDate,
                                                     @RequestParam(required = false) String districtName) {
        return programService.getProgramStatusSummery(agencyId, DateUtil.covertStringToDate(fromDate),DateUtil.covertStringToDate(toDate),districtName );
    }

    @GetMapping("/status-list/{agencyId}")
    public WorkflowResponse getProgramsByStatus(@PathVariable Long agencyId,
                                                @RequestParam List<String> statuses) {

        List<Program> programs = programRepository.findByAgencyAgencyIdAndStatusIn(agencyId, statuses);

        List<ProgramResponse> response = programs != null ? programs.stream().map(ProgramResponseMapper::map).collect(Collectors.toList()) : null;
        return WorkflowResponse.builder().message("Success").status(200).data(response).build();

    }

    @PutMapping("/update/{programId}")
    public ResponseEntity<?> updateProgram(@PathVariable Long programId, @RequestBody ProgramRequestDto request, Principal principal) {
        try {
            logService.logs(principal != null ? principal.getName() : "system", "UPDATE", "Program updated successfully | ID: " + programId, "Program", "/program/update/" + programId);
            return ResponseEntity.ok(programService.updateProgramStatus(programId, request));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
}
