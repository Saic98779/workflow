package com.metaverse.workflow.notifications.controller;

import com.metaverse.workflow.enums.NotificationStatus;
import com.metaverse.workflow.model.Notifications;
import com.metaverse.workflow.notifications.dto.*;
import com.metaverse.workflow.notifications.service.NotificationServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    // 1. Call Center -> Agency
    @SuppressWarnings("unused")
    @Operation(
            summary = "Send Notification from Call Center to Agency",
            description = "Creates a new notification initiated by a call center agent to an agency."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification sent successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/problem+json"))
    })
    @PostMapping("/send/callcenter-to-agency")
    public ResponseEntity<NotificationResponseDto> sendFromCallCenterToAgency(
            @RequestBody NotificationRequestDto dto) {
        Notifications notification = notificationService.sendFromCallCenterToAgency(dto);
        return ResponseEntity.ok(NotificationMapper.toDto(notification));
    }

    // 2. Agency -> Call Center
    @SuppressWarnings("unused")
    @Operation(
            summary = "Send Notification from Agency to Call Center",
            description = "Creates a new notification initiated by an agency to a call center agent."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification sent successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/problem+json"))
    })
    @PostMapping("/send/agency-to-callcenter")
    public ResponseEntity<NotificationResponseDto> sendFromAgencyToCallCenter(
            @RequestBody NotificationRequestDto dto) {
        Notifications notification = notificationService.sendFromAgencyToCallCenter(dto);
        return ResponseEntity.ok(NotificationMapper.toDto(notification));
    }

    // 3. Get all notifications by Agency and optional list of statuses
    @Operation(
            summary = "Get Notifications by Agency",
            description = "Fetches all notifications assigned to the specified agency. "
                    + "You can optionally filter by a list of NotificationStatus values."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Agency not found",
                    content = @Content(mediaType = "application/problem+json"))
    })
    @GetMapping("/agency/{agencyId}")
    public ResponseEntity<List<NotificationResponseDto>> getByAgency(
            @PathVariable Long agencyId,
            @RequestParam(required = false) List<NotificationStatus> statuses
    ) {
        List<Notifications> list = (statuses == null || statuses.isEmpty())
                ? notificationService.getAllByAgency(agencyId)
                : notificationService.getAllByAgencyAndStatuses(agencyId, statuses);

        return ResponseEntity.ok(list.stream()
                .map(NotificationMapper::toDto)
                .toList());
    }

    // 4. Get all notifications by Call Center Agent
    @SuppressWarnings("unused")
    @Operation(
            summary = "Get Notifications by Call Center Agent",
            description = "Fetches all notifications assigned to a specific call center agent (userId). "
                    + "You can optionally filter by a list of NotificationStatus values."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Call center agent not found",
                    content = @Content(mediaType = "application/problem+json"))
    })
    @GetMapping("/callcenter/{userId}")
    public ResponseEntity<List<NotificationResponseDto>> getByCallCenter(
            @PathVariable String userId,
            @RequestParam(required = false) List<NotificationStatus> statuses
    ) {
        List<Notifications> list = (statuses == null || statuses.isEmpty())
                ? notificationService.getAllByCallCenterUser(userId)
                : notificationService.getAllByCallCenterUserAndStatuses(userId, statuses);

        return ResponseEntity.ok(list.stream()
                .map(NotificationMapper::toDto)
                .toList());
    }

    @GetMapping("/callcenter/remarks/{userId}")
    public ResponseEntity<Map<String, List<RemarkDto>>> getAllCallCenterRemarks(
            @PathVariable String userId,
            @RequestParam(required = false) List<NotificationStatus> statuses
    ) {
        List<Notifications> list = (statuses == null || statuses.isEmpty())
                ? notificationService.getAllByCallCenterUser(userId)
                : notificationService.getAllByCallCenterUserAndStatuses(userId, statuses);

        // Flatten all remarks and map NotificationRemark -> RemarkDto
        List<RemarkDto> allRemarks = list.stream()
                .flatMap(n -> n.getRemarksByAgency().stream())
                .map(r -> new RemarkDto(r.getRemarkText(), r.getRemarkedAt()))
                .toList();

        Map<String, List<RemarkDto>> response = Map.of("remarks", allRemarks);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/agency/remarks/{agencyId}")
    public ResponseEntity<Map<String, List<RemarkDto>>> getAllAgencyRemarks(
            @PathVariable Long agencyId,
            @RequestParam(required = false) List<NotificationStatus> statuses
    ) {
        List<Notifications> list = (statuses == null || statuses.isEmpty())
                ? notificationService.getAllByAgency(agencyId)
                : notificationService.getAllByAgencyAndStatuses(agencyId, statuses);

        // Flatten all remarks and map NotificationRemark -> RemarkDto
        List<RemarkDto> allRemarks = list.stream()
                .flatMap(n -> n.getRemarksByCallCenter().stream())
                .map(r -> new RemarkDto(r.getRemarkText(), r.getRemarkedAt()))
                .toList();

        Map<String, List<RemarkDto>> response = Map.of("remarks", allRemarks);

        return ResponseEntity.ok(response);
    }



    @Operation(
            summary = "Update Notification Status",
            description = "Updates the status of a notification by its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification status updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NotificationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content(mediaType = "application/problem+json"))
    })
    @PatchMapping("/status")
    public ResponseEntity<NotificationResponseDto> updateStatus(
            @RequestBody NotificationStatusUpdateDto dto) {

        Notifications updated = notificationService.updateStatus(dto);
        return ResponseEntity.ok(NotificationMapper.toDto(updated));
    }

}
