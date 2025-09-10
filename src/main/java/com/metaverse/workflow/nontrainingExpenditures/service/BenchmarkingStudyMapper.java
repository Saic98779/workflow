package com.metaverse.workflow.nontrainingExpenditures.service;


import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.BenchmarkingStudy;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.nontrainingExpenditures.Dto.BenchmarkingStudyRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.BenchmarkingStudyResponse;

public class BenchmarkingStudyMapper {

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    public static BenchmarkingStudy mapToThrustSectorVisitReq(BenchmarkingStudyRequest request, NonTrainingSubActivity nonTrainingSubActivity) {
        if (request == null) return null;

        BenchmarkingStudy entity = new BenchmarkingStudy();
        entity.setThrustSectorName(request.getThrustSectorName());
        entity.setNameOfTheClusterMapped(request.getNameOfTheClusterMapped());
        entity.setNameOfTheMSMEVisited(request.getNameOfTheMSMEVisited());
        entity.setDateOfVisit(DateUtil.covertStringToDate(request.getDateOfVisit()));
        entity.setPaymentDate(DateUtil.covertStringToDate(request.getPaymentDate()));
        entity.setBillDate(DateUtil.covertStringToDate(request.getBillDate()));
        entity.setExpenditureAmount(request.getExpenditureAmount());
        entity.setBillNo(request.getBillNo());
        entity.setPayeeName(request.getPayeeName());
        entity.setAccountNumber(request.getAccountNumber());
        entity.setBankName(request.getBankName());
        entity.setIfscCode(request.getIfscCode());
        entity.setModeOfPayment(request.getModeOfPayment());
        entity.setTransactionId(request.getTransactionId());
        entity.setPurpose(request.getPurpose());
        entity.setUploadBillUrl(request.getUploadBillUrl());
        entity.setModeOfTravel(request.getModeOfTravel());
        entity.setNameOfTheMSMEVisited(request.getNameOfTheMSMEVisited());
        entity.setReportSubmissionDate(DateUtil.covertStringToDate(request.getReportSubmissionDate()));
        entity.setNonTrainingSubActivity(nonTrainingSubActivity);
        return entity;
    }

    public static BenchmarkingStudyResponse mapToThrustSectorVisitRes(BenchmarkingStudy entity) {
        if (entity == null) return null;

        BenchmarkingStudyResponse response = new BenchmarkingStudyResponse();
        response.setThrustSectorVisitId(entity.getBenchmarkingStudyId());
        response.setThrustSectorName(entity.getThrustSectorName());
        response.setNameOfTheClusterMapped(entity.getNameOfTheClusterMapped());
        response.setNameOfTheMSMEVisited(entity.getNameOfTheMSMEVisited());
        response.setDateOfVisit(DateUtil.dateToString(entity.getDateOfVisit(), DATE_PATTERN));
        response.setPaymentDate(DateUtil.dateToString(entity.getPaymentDate(), DATE_PATTERN));
        response.setBillDate(DateUtil.dateToString(entity.getBillDate(), DATE_PATTERN));
        response.setExpenditureAmount(entity.getExpenditureAmount());
        response.setBillNo(entity.getBillNo());
        response.setPayeeName(entity.getPayeeName());
        response.setAccountNumber(entity.getAccountNumber());
        response.setBankName(entity.getBankName());
        response.setIfscCode(entity.getIfscCode());
        response.setModeOfPayment(entity.getModeOfPayment());
        response.setTransactionId(entity.getTransactionId());
        response.setPurpose(entity.getPurpose());
        response.setUploadBillUrl(entity.getUploadBillUrl());
        response.setModeOfTravel(entity.getModeOfTravel());
        response.setNameOfTheBestPerformingState(entity.getNameOfTheBestPerformingState());
        response.setReportSubmissionDate(DateUtil.dateToString(entity.getReportSubmissionDate(), DATE_PATTERN));

        return response;
    }
    public static BenchmarkingStudy mapToUpdateBenchmarkingStudy(BenchmarkingStudy entity,BenchmarkingStudyRequest request) {

        if (request == null) return entity;

        entity.setThrustSectorName(request.getThrustSectorName() != null ? request.getThrustSectorName() : entity.getThrustSectorName());
        entity.setNameOfTheClusterMapped(request.getNameOfTheClusterMapped() != null ? request.getNameOfTheClusterMapped() : entity.getNameOfTheClusterMapped());
        entity.setNameOfTheMSMEVisited(request.getNameOfTheMSMEVisited() != null ? request.getNameOfTheMSMEVisited() : entity.getNameOfTheMSMEVisited());

        entity.setDateOfVisit(request.getDateOfVisit() != null ? DateUtil.covertStringToDate(request.getDateOfVisit()) : entity.getDateOfVisit());
        entity.setPaymentDate(request.getPaymentDate() != null ? DateUtil.covertStringToDate(request.getPaymentDate()) : entity.getPaymentDate());
        entity.setBillDate(request.getBillDate() != null ? DateUtil.covertStringToDate(request.getBillDate()) : entity.getBillDate());

        entity.setExpenditureAmount(request.getExpenditureAmount() != null ? request.getExpenditureAmount() : entity.getExpenditureAmount());
        entity.setBillNo(request.getBillNo() != null ? request.getBillNo() : entity.getBillNo());
        entity.setPayeeName(request.getPayeeName() != null ? request.getPayeeName() : entity.getPayeeName());
        entity.setAccountNumber(request.getAccountNumber() != null ? request.getAccountNumber() : entity.getAccountNumber());
        entity.setBankName(request.getBankName() != null ? request.getBankName() : entity.getBankName());
        entity.setIfscCode(request.getIfscCode() != null ? request.getIfscCode() : entity.getIfscCode());
        entity.setModeOfPayment(request.getModeOfPayment() != null ? request.getModeOfPayment() : entity.getModeOfPayment());
        entity.setTransactionId(request.getTransactionId() != null ? request.getTransactionId() : entity.getTransactionId());
        entity.setPurpose(request.getPurpose() != null ? request.getPurpose() : entity.getPurpose());
        entity.setUploadBillUrl(request.getUploadBillUrl() != null ? request.getUploadBillUrl() : entity.getUploadBillUrl());
        entity.setModeOfTravel(request.getModeOfTravel() != null ? request.getModeOfTravel() : entity.getModeOfTravel());
        entity.setReportSubmissionDate(request.getReportSubmissionDate() != null ? DateUtil.covertStringToDate(request.getReportSubmissionDate()) : entity.getReportSubmissionDate());

        return entity;
    }

}

