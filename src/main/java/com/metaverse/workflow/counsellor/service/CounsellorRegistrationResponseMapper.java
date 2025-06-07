package com.metaverse.workflow.counsellor.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.CounsellorRegistration;
import org.springframework.stereotype.Component;

@Component
public class CounsellorRegistrationResponseMapper {
    public static CounsellorRegistrationResponse map(CounsellorRegistration counsellorRegistration)
    {
        return CounsellorRegistrationResponse.builder().counsellorRegistrationId(counsellorRegistration.getCounsellorRegistrationId())
                .dateOfRegistration(DateUtil.dateToString(counsellorRegistration.getDateOfRegistration(), "dd-MM-yyyy"))
                .nameOfCounsellor(counsellorRegistration.getNameOfCounsellor())
                .dateOfBirth(DateUtil.dateToString(counsellorRegistration.getDateOfBirth(), "dd-MM-yyyy"))
                .gender(counsellorRegistration.getGender())
                .socialCategory((counsellorRegistration.getSocialCategory()))
                .educationalQualification(counsellorRegistration.getEducationalQualification())
                .districtId(counsellorRegistration.getRegistrationMandal().getDistrict().getDistrictId())
                .districtName(counsellorRegistration.getRegistrationMandal().getDistrict().getDistrictName())
                .mandalId(counsellorRegistration.getRegistrationMandal().getMandalId())
                .mandalName(counsellorRegistration.getRegistrationMandal().getMandalName())
                .allocatedMandalId(counsellorRegistration.getCounsellorAllotments().get(0).getAllotedMandal().getMandalId())
                .allocatedMandalName(counsellorRegistration.getCounsellorAllotments().get(0).getAllotedMandal().getMandalName())
                .village(counsellorRegistration.getVillage())
                .houseNo(counsellorRegistration.getHouseNo())
                .streetName(counsellorRegistration.getStreetName())
                .pincode(counsellorRegistration.getPincode())
                .landmark(counsellorRegistration.getLandmark())
                .experience(counsellorRegistration.getExperience())
                .designation(counsellorRegistration.getDesignation())
                .specialization(counsellorRegistration.getSpecialization())
                .contactNo(counsellorRegistration.getContactNo())
                .altContactNo(counsellorRegistration.getAltContactNo())
                .emailId(counsellorRegistration.getEmailId())
                .build();
    }
}
