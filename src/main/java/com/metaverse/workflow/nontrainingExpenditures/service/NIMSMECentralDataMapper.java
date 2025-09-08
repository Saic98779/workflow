package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.NIMSMECentralData;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import com.metaverse.workflow.nontrainingExpenditures.Dto.CentralDataRequest;
import com.metaverse.workflow.nontrainingExpenditures.Dto.CentralDataResponse;

import java.util.Objects;

public class NIMSMECentralDataMapper {

    // Request -> Entity
    public static NIMSMECentralData mapToCentralDataReq(
            CentralDataRequest request,
            NonTrainingSubActivity subActivity) {

        if (Objects.isNull(request)) return null;

        NIMSMECentralData entity = new NIMSMECentralData();
        entity.setNonTrainingSubActivity(subActivity);
        entity.setNoOfFileUploaded(request.getNoOfFileUploaded());
        entity.setCost(request.getCost());
        entity.setBillNo(request.getBillNo());
        entity.setBillDate(DateUtil.covertStringToDate(request.getBillDate()));
        entity.setPayeeName(request.getPayeeName());
        entity.setBankName(request.getBankName());
        entity.setIfscCode(request.getIfscCode());
        entity.setModeOfPayment(request.getModeOfPayment());
        entity.setTransactionId(request.getTransactionId());
        entity.setPurpose(request.getPurpose());
        entity.setVerified(request.getVerified());
        entity.setCheckNo(request.getCheckNo());
        entity.setCheckDate(DateUtil.covertStringToDate(request.getCheckDate()));


        return entity;
    }

    // Entity -> Response
    public static CentralDataResponse mapToCentralDataRes(NIMSMECentralData entity) {

        if (Objects.isNull(entity)) return null;

        CentralDataResponse res = new CentralDataResponse();
        res.setId(entity.getCentralDataId());
        res.setSubActivityId(
                entity.getNonTrainingSubActivity() != null
                        ? entity.getNonTrainingSubActivity().getSubActivityId()
                        : null
        );
        res.setSubActivityName(
                entity.getNonTrainingSubActivity() != null
                        ? entity.getNonTrainingSubActivity().getSubActivityName()
                        : null
        );
        res.setNoOfFileUploaded(entity.getNoOfFileUploaded());
        res.setCost(entity.getCost());
        res.setBillNo(entity.getBillNo());
        res.setBillDate(DateUtil.dateToString(entity.getBillDate(),"dd-MM-YYYY"));
        res.setPayeeName(entity.getPayeeName());
        res.setBankName(entity.getBankName());
        res.setIfscCode(entity.getIfscCode());
        res.setModeOfPayment(entity.getModeOfPayment());
        res.setTransactionId(entity.getTransactionId());
        res.setPurpose(entity.getPurpose());
        res.setVerified(entity.getVerified());
        res.setCheckNo(entity.getCheckNo());
        res.setCheckDate(DateUtil.dateToString(entity.getCheckDate(),"dd-MM-YYYY"));
        res.setUploadBillUrl(entity.getUploadBillUrl());

        return res;
    }
}
