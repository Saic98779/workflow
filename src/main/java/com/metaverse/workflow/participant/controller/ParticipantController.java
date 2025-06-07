package com.metaverse.workflow.participant.controller;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.ExcelHelper;
import com.metaverse.workflow.model.Participant;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.participant.repository.ParticipantRepository;
import com.metaverse.workflow.participant.service.ParticipantRequest;
import com.metaverse.workflow.participant.service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ParticipantController {
	
	@Autowired
	private ParticipantService participantService;

	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private ExcelHelper excelHelper;

	@Autowired
	private ParticipantRepository participantRepository;

	@PostMapping("/participant/save")
	public ResponseEntity<WorkflowResponse> saveParticipant(@RequestBody ParticipantRequest participantRequest)
	{
		WorkflowResponse response = participantService.saveParticipant(participantRequest);
		return ResponseEntity.ok(response);
	}
	@PostMapping("/updateParticipant")
	public ResponseEntity<WorkflowResponse> updateParticipant(@RequestBody ParticipantRequest participantRequest)
	{
		WorkflowResponse response = participantService.updateParticipant(participantRequest);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/participants")
	public ResponseEntity<WorkflowResponse> getParticipants()
	{
		WorkflowResponse response = participantService.getParticipants();
		return ResponseEntity.ok(response);
	}
	@GetMapping("getParticipantsByMobileNo/{mobileNo}")
	public ResponseEntity<WorkflowResponse> getParticipantsByMobileNo(@PathVariable Long mobileNo)
	{
		WorkflowResponse response=	participantService.getParticipantsByMobileNo(mobileNo);
		return ResponseEntity.ok(response);
	}
	@GetMapping("/getOrganizationByParticipant/{participantId}")
	public ResponseEntity<WorkflowResponse> getOrganizationByParticipant(@PathVariable Long participantId)
	{
		WorkflowResponse response = participantService.getOrganizationByParticipant(participantId);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/getParticipantByTypeOfProgram/{typeOfProgram}")
	public ResponseEntity<WorkflowResponse> getParticipantByTypeOfProgram(@PathVariable String typeOfProgram)
	{
		WorkflowResponse response = participantService.getParticipantByTypeOfProgram(typeOfProgram);
		return ResponseEntity.ok(response);
	}
	@GetMapping("getParticipantById/{id}")
	public  ResponseEntity<WorkflowResponse> getParticipantById(@PathVariable Long id) {
		WorkflowResponse response=participantService.getParticipantsByParticipantId(id);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/participant/mobileno/exist/{mobileNo}")
	public ResponseEntity<Boolean> isParticipantsMobileNoExist(@PathVariable Long mobileNo)
	{
		Boolean mobileNumberExists = participantService.isMobileNumberExists(mobileNo);
		return ResponseEntity.ok(mobileNumberExists);
	}

	@PostMapping(value = "/participants/import", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
	public WorkflowResponse uploadExcel(@RequestPart("file") MultipartFile file,
										@RequestParam("programId")Long programId) {
		try {
			if (!file.getOriginalFilename().endsWith(".xlsx")) {
				return WorkflowResponse.builder().message(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).status(200).data("Invalid file format").build();
			}

			excelHelper.excelToParticipants(file.getInputStream(),programId);
			return WorkflowResponse.builder().message("Success").status(200).data(null).build();
		} catch (Exception e) {
			return WorkflowResponse.builder().message(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)).status(200).data("Error uploading file: " + e.getMessage()).build();
		}
	}
}
