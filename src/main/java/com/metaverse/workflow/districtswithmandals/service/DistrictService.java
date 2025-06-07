package com.metaverse.workflow.districtswithmandals.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.District;
import com.metaverse.workflow.model.Mandal;

import java.util.List;

public interface DistrictService {
    WorkflowResponse saveDistrict(DistrictRequest districtRequest);
    WorkflowResponse getAllDistricts();
    WorkflowResponse getAllMandalOfDistrict(Integer id);
    WorkflowResponse getDistrictById(Integer id);
    WorkflowResponse getAllMandals();
    WorkflowResponse getAllPanchayatByMandalId(Integer mandalId);
    List<District> getAllDistrictsEntity();
    List<Mandal> getAllMandalsEntity();

    Mandal getMandalById(Integer id);

    WorkflowResponse getAllMandalOfDistrictName(String name);
}
