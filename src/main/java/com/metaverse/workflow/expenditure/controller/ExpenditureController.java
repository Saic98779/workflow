package com.metaverse.workflow.expenditure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.metaverse.workflow.activitylog.ActivityLogService;
import com.metaverse.workflow.common.enums.ExpenditureType;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.CommonUtil;
import com.metaverse.workflow.common.util.RestControllerBase;
import com.metaverse.workflow.enums.BillRemarksStatus;
import com.metaverse.workflow.exceptions.*;
import com.metaverse.workflow.expenditure.service.*;
import com.metaverse.workflow.model.HeadOfExpense;
import io.swagger.v3.oas.annotations.Operation;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.security.PrivateKey;
import java.util.List;

@RestController
public class ExpenditureController {
    @Autowired
    ExpenditureService expenditureService;
    @Autowired
    private ActivityLogService logService;

    @PostMapping("/bulk/expenditure/save")
    public ResponseEntity<?> saveBulkExpenditure(Principal principal, @RequestPart String request, @RequestPart(required = false) List<MultipartFile> files) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BulkExpenditureRequest bulkExpenditureRequest = objectMapper.readValue(request, BulkExpenditureRequest.class);
            logService.logs(principal.getName(),"save","adding bulk expenditure for "+ CommonUtil.agencyMap.get(bulkExpenditureRequest.getAgencyId()),"bulk expenditure","/bulk/expenditure/save");
            return ResponseEntity.ok(expenditureService.saveBulkExpenditure(bulkExpenditureRequest, files));
        }
        catch(DataException exception)
        {
            return RestControllerBase.error(exception);
        } 
    }
    @PostMapping("/bulk/expenditure/update/{id}")
    public ResponseEntity<?> updateBulkExpenditure(Principal principal,@PathVariable("id") Long expenditureId, @RequestPart String request, @RequestPart(required = false) List<MultipartFile> files
    ) throws JsonProcessingException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            BulkExpenditureRequest bulkExpenditureRequest = objectMapper.readValue(request, BulkExpenditureRequest.class);
            logService.logs(principal.getName(),"update","update bulk expenditure ","bulk expenditure","/bulk/expenditure/update/{id}");
            return ResponseEntity.ok(expenditureService.updateBulkExpenditure(expenditureId, bulkExpenditureRequest, files));
        } catch (DataException exception) {
            return RestControllerBase.error(exception);
        }
    }
    @PostMapping("/bulk/expenditure/delete/{expenditureId}")
    public ResponseEntity<?> deleteBulkExpenditure(@PathVariable Long expenditureId,Principal principal) {
        try {
            WorkflowResponse response = expenditureService.deleteBulkExpenditure(expenditureId);
            logService.logs(principal.getName(),"delete","delete bulk expenditure ","bulk expenditure","/bulk/expenditure/delete/{expenditureId}");
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
    public ResponseEntity<?> saveProgramExpenditure(Principal principal,
            @RequestPart("request") String request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws ParseException {
        try {
            JSONParser parser = new JSONParser();
            ProgramExpenditureRequest programExpenditureRequest = parser.parse(request, ProgramExpenditureRequest.class);
            var response = expenditureService.saveProgramExpenditure(programExpenditureRequest, files);
            logService.logs(principal.getName(),"save","save program expenditure ","program expenditure","/program/expenditure/save");
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
    public ResponseEntity<?> updateProgramExpenditure(Principal principal,
            @PathVariable("expenditureId") Long expenditureId,
            @RequestPart("request") String request,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws ParseException {
        try {
            JSONParser parser = new JSONParser();
            ProgramExpenditureRequest programExpenditureRequest = parser.parse(request, ProgramExpenditureRequest.class);
            var response = expenditureService.updateProgramExpenditure(expenditureId, programExpenditureRequest, files);
            logService.logs(principal.getName(),"update","update program expenditure ","program expenditure","/program/expenditure/update/{expenditureId}");
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
    public ResponseEntity<?> saveTransaction(Principal principal,
            @RequestBody BulkExpenditureTransactionRequest request) throws DataException {
        try {
            BulkExpenditureTransactionResponse response = expenditureService.saveTransaction(request);
            logService.logs(principal.getName(), "save","Fetching item from bulk stock and saving transaction","bulk transaction","/bulk/transactions/save");
            return ResponseEntity.ok(response);
        }
        catch (DataException ex) {
            return RestControllerBase.error(ex);
        }
    }
    @PostMapping("/bulk/transactions/delete/{transactionId}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long transactionId,Principal principal) throws DataException {
        try {
            WorkflowResponse response = expenditureService.deleteTransaction(transactionId);
            logService.logs(principal.getName(), "DELETE", "Removing item from bulk stock and deleting transaction", "Bulk Transaction", "/bulk/transactions/delete/{transactionId}");
            return ResponseEntity.ok(response);
        }
        catch (DataException ex) {
            return RestControllerBase.error(ex);
        }
    }
    @PostMapping("/bulk/transactions/update/{transactionId}")
    public ResponseEntity<?> updateTransaction(Principal  principal,@PathVariable Long transactionId,
            @RequestBody BulkExpenditureTransactionRequest request) throws DataException {
        try {
            WorkflowResponse response = expenditureService.updateTransaction(transactionId,request);
            logService.logs(principal.getName(), "UPDATE", "Modifying bulk stock transaction details", "Bulk Transaction", "/bulk/transactions/update");
            return ResponseEntity.ok(response);
        }
        catch (DataException ex) {
            return RestControllerBase.error(ex);
        }
    }

    @PostMapping("/bulk/transactions/lookup")
    public ResponseEntity<?> getExpendituresByExpenseAndItem(Principal principal,
            @RequestBody BulkExpenditureLookupRequest request) throws DataException {
        try {
            BulkExpenditureLookupResponse result = expenditureService.getBulkExpendituresByExpenseAndItem(request);
            logService.logs(principal.getName(), "LOOKUP", "Fetching bulk expenditures by expense and item", "Bulk Transaction", "/bulk/transactions/lookup");
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
    public ResponseEntity<?> deleteProgramExpenditure(@PathVariable Long expenditureId,Principal principal) {
        try {
            WorkflowResponse response = expenditureService.deleteProgramExpenditure(expenditureId);
            logService.logs(principal.getName(), "DELETE", "Deleting program expenditure with ID: " + expenditureId, "Program Expenditure", "/program/expenditure/delete/" + expenditureId);
            return ResponseEntity.ok(response);
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @GetMapping("/combined/expenditure/{programId}")
    public ResponseEntity<?> getCombinedExpenditure(@PathVariable Long programId)
    {
        try {
            return  ResponseEntity.ok( expenditureService.getExpenditureHeadOfExpenseWise(programId));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }
    @PutMapping("/save/remarks")
    public ResponseEntity<?> addingRemarks(Principal principal,@RequestBody ExpenditureRemarksDTO remarksDTO,
                                           @RequestParam(value = "status", required = false) BillRemarksStatus status) {
        try {
            logService.logs(principal.getName(), "UPDATE", "Adding/updating remarks for expenditure with status: " + status, "Program Expenditure", "/save/remarks");
            return  ResponseEntity.ok(expenditureService.addRemarkOrResponse(remarksDTO, status));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

    @PutMapping("/save/remarks/transaction")
    public ResponseEntity<?> addingRemarksTransaction(@RequestBody ExpenditureRemarksDTO remarksDTO, @RequestParam("status") BillRemarksStatus status, Principal principal)
    {
        try {
            logService.logs(principal.getName(), "UPDATE", "Adding/updating remarks for transaction with status: " + status, "Bulk Transaction", "/save/remarks/transaction");
            return  ResponseEntity.ok(expenditureService.addRemarkOrResponseTransaction(remarksDTO, status));
        } catch (DataException e) {
            return RestControllerBase.error(e);
        }
    }

}


