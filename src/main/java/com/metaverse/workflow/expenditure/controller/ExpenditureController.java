package com.metaverse.workflow.expenditure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.common.enums.ExpenditureType;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.exceptions.*;
import com.metaverse.workflow.expenditure.service.*;
import com.metaverse.workflow.model.HeadOfExpense;
import com.metaverse.workflow.program.service.ProgramSessionRequest;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class ExpenditureController {
    @Autowired
    ExpenditureService expenditureService;

    @PostMapping("/bulk/expenditure/save")
    public ResponseEntity<?> saveBulkExpenditure(@RequestPart String request, @RequestPart(required = false) List<MultipartFile> files) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BulkExpenditureRequest bulkExpenditureRequest = objectMapper.readValue(request, BulkExpenditureRequest.class);
            return ResponseEntity.ok(expenditureService.saveBulkExpenditure(bulkExpenditureRequest, files));
        }
        catch(DataException exception)
        {
            return RestControllerBase.error(exception);
        } 
    }
    @PostMapping("/bulk/expenditure/update/{id}")
    public ResponseEntity<?> updateBulkExpenditure(@PathVariable("id") Long expenditureId, @RequestPart String request, @RequestPart(required = false) List<MultipartFile> files
    ) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BulkExpenditureRequest bulkExpenditureRequest = objectMapper.readValue(request, BulkExpenditureRequest.class);
            return ResponseEntity.ok(expenditureService.updateBulkExpenditure(expenditureId, bulkExpenditureRequest, files));
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
    }
    @PostMapping("/bulk/expenditure/delete/{expenditureId}")
    public ResponseEntity<?> deleteBulkExpenditure(@PathVariable Long expenditureId) {
        try {
            WorkflowResponse response = expenditureService.deleteBulkExpenditure(expenditureId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }


    @PostMapping(
            value = "/program/expenditure/save",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> saveProgramExpenditure(
            @RequestPart("request") String request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws ParseException {
        try {
            JSONParser parser = new JSONParser();
            ProgramExpenditureRequest programExpenditureRequest = parser.parse(request, ProgramExpenditureRequest.class);
            var response = expenditureService.saveProgramExpenditure(programExpenditureRequest, files);
            return ResponseEntity.ok(response);
        }
        catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
    }
    @PostMapping(
            value = "/program/expenditure/update/{expenditureId}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> updateProgramExpenditure(
            @PathVariable("expenditureId") Long expenditureId,
            @RequestPart("request") String request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws ParseException {
        try {
            JSONParser parser = new JSONParser();
            ProgramExpenditureRequest programExpenditureRequest = parser.parse(request, ProgramExpenditureRequest.class);
            var response = expenditureService.updateProgramExpenditure(expenditureId, programExpenditureRequest, files);
            return ResponseEntity.ok(response);
        }
        catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
    }


    @GetMapping("/program/expenditure/{expenditureType}")
    public ResponseEntity<?> getAllProgramExpenditure(@PathVariable ExpenditureType expenditureType) {
            return ResponseEntity.ok(expenditureService.getAllProgramExpenditure(expenditureType));
    }

    @GetMapping("/bulk/expenditure")
    public ResponseEntity<WorkflowResponse> getAllBulkExpenditure( ) {
        return ResponseEntity.ok(expenditureService.getAllBulkExpenditure());
    }
    @GetMapping("/bulk/expenditure/agency/{agencyId}")
    public ResponseEntity<WorkflowResponse> getAllBulkExpenditureByAgency(@PathVariable Long agencyId) {
        return ResponseEntity.ok(expenditureService.getAllBulkExpenditureByAgencyId(agencyId));
    }

    @GetMapping("/program/expenditure")
    public ResponseEntity<?> getAllProgramExpenditure(
            @RequestParam ExpenditureType expenditureType,
            @RequestParam Long programId) {
        return ResponseEntity.ok(expenditureService.getAllProgramExpenditureByProgram(expenditureType, programId));
    }
    @GetMapping("/program/expenditure/agency")
    public ResponseEntity<?> getAllProgramExpenditureByAgencyId(
            @RequestParam ExpenditureType expenditureType,
            @RequestParam Long agencyId,
            @RequestParam Long programId) {
        return ResponseEntity.ok(expenditureService.getAllProgramExpenditureByProgramIdByAgencyId(expenditureType,agencyId,programId));
    }


    @PostMapping("/bulk/transactions/save")
    public ResponseEntity<?> saveTransaction(
            @RequestBody BulkExpenditureTransactionRequest request) throws DataException {
        try {
            BulkExpenditureTransactionResponse response = expenditureService.saveTransaction(request);
            return ResponseEntity.ok(response);
        }
        catch (DataException ex) {
            return RestControllerBase.error(ex);
        }
    }
    @PostMapping("/bulk/transactions/delete/{transactionId}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long transactionId) throws DataException {
        try {
            WorkflowResponse response = expenditureService.deleteTransaction(transactionId);
            return ResponseEntity.ok(response);
        }
        catch (DataException ex) {
            return RestControllerBase.error(ex);
        }
    }
    @PostMapping("/bulk/transactions/update/{transactionId}")
    public ResponseEntity<?> updateTransaction(@PathVariable Long transactionId,
            @RequestBody BulkExpenditureTransactionRequest request) throws DataException {
        try {
            WorkflowResponse response = expenditureService.updateTransaction(transactionId,request);
            return ResponseEntity.ok(response);
        }
        catch (DataException ex) {
            return RestControllerBase.error(ex);
        }
    }

    @PostMapping("/bulk/transactions/lookup")
    public ResponseEntity<?> getExpendituresByExpenseAndItem(
            @RequestBody BulkExpenditureLookupRequest request) throws DataException {
        try {
            BulkExpenditureLookupResponse result = expenditureService.getBulkExpendituresByExpenseAndItem(request);
            return ResponseEntity.ok(result);
        }
        catch (DataException ex) {
            return RestControllerBase.error(ex);
        }
    }

    @GetMapping("/bulk/transactions/items")
    public ResponseEntity<List<String>> getItemsByExpense(@RequestParam Integer expenseId) throws DataException {
        List<String> items = expenditureService.getItemsByHeadOfExpense(expenseId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/bulk/transactions")
    public ResponseEntity<WorkflowResponse> getAllBulkExpenditureTransactionByProgram(
            @RequestParam Long programId) {
        return ResponseEntity.ok(expenditureService.getAllBulkExpenditureTransactionByProgram(programId));
    }
    @GetMapping("/expenses")
    public List<HeadOfExpense> getAllExpenses()
    {
        return expenditureService.getAllHeadOfExpenses();
    }

    @PostMapping("/program/expenditure/delete/{expenditureId}")
    public ResponseEntity<?> deleteProgramExpenditure(@PathVariable Long expenditureId) {
        try {
            WorkflowResponse response = expenditureService.deleteProgramExpenditure(expenditureId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

}


