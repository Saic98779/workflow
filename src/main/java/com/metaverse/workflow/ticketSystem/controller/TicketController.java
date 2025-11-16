package com.metaverse.workflow.ticketSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.enums.TicketStatus;
import com.metaverse.workflow.model.Ticket;
import com.metaverse.workflow.ticketSystem.dto.TicketDto;
import com.metaverse.workflow.ticketSystem.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // ----------------------------------------------------------------------
    // CREATE TICKET
    // ----------------------------------------------------------------------
    @Operation(
            summary = "Create a new ticket",
            description = "Creates a new support ticket with optional attachments. "
                    + "Send the ticket as JSON string (field name: 'ticket') and files as multipart data."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkflowResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<WorkflowResponse> createTicket(
            @Parameter(description = "Ticket details as JSON string", required = true)
            @RequestPart("ticket") String ticketDto,
            @Parameter(description = "Optional file attachments for the ticket")
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        try {
            TicketDto ticketDtos = objectMapper.readValue(ticketDto, TicketDto.class);

            WorkflowResponse response = ticketService.createTicket(ticketDtos, files);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    WorkflowResponse.error("Invalid ticket JSON: " + e.getMessage())
            );
        }
    }

    // ----------------------------------------------------------------------
// GET ALL TICKETS (with pagination + status filter)
// ----------------------------------------------------------------------
    @Operation(
            summary = "Get all tickets",
            description = "Fetches all tickets with optional status filter and pagination."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tickets fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkflowResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    public ResponseEntity<WorkflowResponse> getAllTickets(
            @Parameter(description = "Ticket statuses to filter (e.g., OPEN, CLOSED). If not provided, both are returned.")
            @RequestParam(value = "statusFilter", required = false) List<TicketStatus> statusFilter,

            @Parameter(description = "Page number (default: 0)")
            @RequestParam(value = "page", required = false) Integer page,

            @Parameter(description = "Page size (default: 10)")
            @RequestParam(value = "size", required = false) Integer size
    ) {

        WorkflowResponse response = ticketService.getAllTickets(
                statusFilter != null ? statusFilter : List.of(TicketStatus.OPEN, TicketStatus.CLOSED),
                page != null ? page : 0,
                size != null ? size : 10
        );

        return ResponseEntity.ok(response);
    }


    // ----------------------------------------------------------------------
    // UPDATE TICKET
    // ----------------------------------------------------------------------
    @Operation(
            summary = "Update an existing ticket",
            description = "Updates ticket details (status, priority, assignee, etc.) and optionally adds comments."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkflowResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{ticketId}")
    public ResponseEntity<WorkflowResponse> updateTicket(
            @Parameter(description = "ID of the ticket to update", required = true)
            @PathVariable("ticketId") String ticketId,
            @Parameter(description = "Updated ticket details in JSON format", required = true)
            @RequestBody TicketDto ticketDto) {

        WorkflowResponse response = ticketService.updateTicket(ticketId, ticketDto);
        return ResponseEntity.ok(response);
    }

    // ----------------------------------------------------------------------
    // GET TICKET BY ID
    // ----------------------------------------------------------------------
    @Operation(
            summary = "Get ticket by ID",
            description = "Fetches a single ticket by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkflowResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content)
    })
    @GetMapping("/{ticketId}")
    public ResponseEntity<WorkflowResponse> getTicketById(
            @Parameter(description = "ID of the ticket to fetch", required = true)
            @PathVariable("ticketId") Long ticketId) {

        WorkflowResponse response = ticketService.getTicketById(ticketId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get report by ID",
            description = "Fetches a single ticket by its unique identifier."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Report fetched successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkflowResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ticket not found", content = @Content)
    })
    @GetMapping("report/{reportId}")
    public ResponseEntity<WorkflowResponse> getReportById(
            @Parameter(description = "Page number (default: 0)") @RequestParam(value = "page", required = false) Integer page,
            @Parameter(description = "Page size (default: 10)") @RequestParam(value = "size", required = false) Integer size,
            @Parameter(description = "ID of the report to fetch", required = true)
            @PathVariable("reportId") String reportId) {
        WorkflowResponse response = ticketService.getReportById(page != null ? page : 0, size != null ? size : 10, reportId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<WorkflowResponse> getTicketsForUser(
            @PathVariable String userId,
            @RequestParam List<TicketStatus> statusFilter,   // multiple statuses supported
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        WorkflowResponse response = ticketService.getTicketsForUser(
                userId,
                statusFilter,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        return ResponseEntity.ok(response);
    }


    @GetMapping("/roles/{role}/statuses")
    public ResponseEntity<List<TicketStatus>> getStatusesForRole(@PathVariable String role) {
        List<TicketStatus> statuses = ticketService.getStatusesForRole(role);
        return ResponseEntity.ok(statuses);
    }


}
