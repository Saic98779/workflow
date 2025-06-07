package com.metaverse.workflow.counsellor.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.counsellor.repository.CounsellorAllotmentRepository;
import com.metaverse.workflow.counsellor.repository.CounsellorRegistrationRepository;
import com.metaverse.workflow.districtswithmandals.repository.DistrictRepository;
import com.metaverse.workflow.districtswithmandals.repository.MandalRepositrory;
import com.metaverse.workflow.model.CounsellorAllotment;
import com.metaverse.workflow.model.CounsellorRegistration;
import com.metaverse.workflow.model.Mandal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CounsellorServiceImpl implements CounsellorService {
    @Autowired
    CounsellorRegistrationRepository counsellorRegistrationRepository;

    @Autowired
    CounsellorAllotmentRepository counsellorAllotmentRepository;

    @Autowired
    DistrictRepository districtRepository;

    @Autowired
    MandalRepositrory mandalRepositrory;

    @Override
    public WorkflowResponse saveCounseller(CounsellorRegistrationRequest counsellorRequest) {
        log.info("service counsellor");
        CounsellorRegistration counsellorRegistration = CounsellorRegistrationRequestMapper.map(counsellorRequest);

        Optional<Mandal> mandal = mandalRepositrory.findById(counsellorRequest.getMandalId());
        if (!mandal.isPresent()) {
            return WorkflowResponse.builder().message("Mandal not found").status(400).build();
        }

        counsellorRegistration.setRegistrationMandal(mandal.get());

        CounsellorAllotment counsellorAllotment = CounsellorAllotmentMapper.map(counsellorRegistration);

        Optional<Mandal> allocatedMandal = mandalRepositrory.findById(counsellorRequest.getAllortedMandalId());
        if (!allocatedMandal.isPresent()) {
            return WorkflowResponse.builder().message("AllortedMandal not found").status(400).build();
        }

        counsellorAllotment.setAllotedMandal(allocatedMandal.get());
        counsellorRegistration.setCounsellorAllotments(Arrays.asList(counsellorAllotment));
        CounsellorRegistration response = counsellorRegistrationRepository.save(counsellorRegistration);
        counsellorAllotment.setCounsellorRegistration(response);
        counsellorAllotmentRepository.save(counsellorAllotment);
        return WorkflowResponse.builder().message("success").status(200).data(CounsellorRegistrationResponseMapper.map(response)).build();
    }




    @Override
    public WorkflowResponse getAllCounsellors() {
        List<CounsellorRegistration> counsellorRegistrationList= counsellorRegistrationRepository.findAll();

        List<CounsellorRegistrationResponse>  counsellorRegistrationResponseList=counsellorRegistrationList.stream().map(counsellorRegistration -> CounsellorRegistrationResponseMapper.map(counsellorRegistration)).collect(Collectors.toList());
        return WorkflowResponse.builder().message("success").status(200).data(counsellorRegistrationResponseList).build();
    }

    @Override
    public WorkflowResponse getCounsellerByMandal(Integer mandalId) {
        Optional<Mandal> mandal = mandalRepositrory.findById(mandalId);
        if (!mandal.isPresent()) {
            return WorkflowResponse.builder().message("Mandal not found").status(400).build();
        }

        List<CounsellorAllotment> counsellorAllotments = counsellorAllotmentRepository.findByAllotedMandal(mandal.get());
        if(counsellorAllotments.isEmpty())return
                WorkflowResponse.builder().status(400).message("Counsellor not allocated for this mandal").build();
        CounsellorRegistration counsellorRegistration = counsellorAllotments.get(0).getCounsellorRegistration();
        CounsellorRegistrationResponse registrationResponse= CounsellorRegistrationResponseMapper.map(counsellorRegistration);
        return WorkflowResponse.builder().message("success").status(200).data(registrationResponse).build();
    }

}
