package com.metaverse.workflow.counsellor.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.CounsellorRegistration;
import org.springframework.stereotype.Component;

@Component
public class CounsellorRegistrationRequestMapper {
    public static CounsellorRegistration map(CounsellorRegistrationRequest counsllorRequest) {
        return CounsellorRegistration.builder()
                .dateOfRegistration(DateUtil.stringToDate(counsllorRequest.getDateOfRegistration(), "dd-MM-yyyy"))
                .nameOfCounsellor(counsllorRequest.getNameOfCounsellor())
                .dateOfBirth(DateUtil.stringToDate(counsllorRequest.getDateOfBirth(), "dd-MM-yyyy"))
                .gender(counsllorRequest.getGender())
                .socialCategory((counsllorRequest.getSocialCategory()))
                .educationalQualification(counsllorRequest.getEducationalQualification())
                .village(counsllorRequest.getVillage())
                .houseNo(counsllorRequest.getHouseNo())
                .streetName(counsllorRequest.getStreetName())
                .pincode(counsllorRequest.getPincode())
                .landmark(counsllorRequest.getLandmark())
                .experience(counsllorRequest.getExpriance())
                .designation(counsllorRequest.getDesignation())
                .specialization(counsllorRequest.getSpecialzation())
                .contactNo(counsllorRequest.getContactNo())
                .altContactNo(counsllorRequest.getAltContactNo())
                .emailId(counsllorRequest.getEmailId())
                .dateOfSelection(DateUtil.stringToDate(counsllorRequest.getDateOfSelection(), "dd-MM-yyyy"))
//                .district(DistrictUtil.districtEntityList.get(counsllorRequest.districtId))
//                .mandal(DistrictUtil.mandalEntityList.get(counsllorRequest.mandalId))
//                .allotedDistrict(DistrictUtil.districtEntityList.get(counsllorRequest.allortedDistrictId))
//                .allotedMandal(DistrictUtil.mandalEntityList.get(counsllorRequest.allortedDistrictId))
                .build();
    }



}
