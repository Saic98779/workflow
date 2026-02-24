package com.metaverse.workflow.ramp.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Program;
import com.metaverse.workflow.model.RampEnrollment;
import com.metaverse.workflow.model.RampRegistration;
import com.metaverse.workflow.ramp.repository.RampEnrollmentRepository;
import com.metaverse.workflow.ramp.repository.RampRegistrationRepository;
import com.metaverse.workflow.program.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RampServiceAdapter implements RampService {

    @Autowired
    private RampEnrollmentRepository rampEnrollmentRepository;

    @Autowired
    private RampRegistrationRepository rampRegistrationRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Override
    public WorkflowResponse saveEnrollment(RampEnrollmentRequest request) {
        RampEnrollment enrollment = mapEnrollment(request);
        RampEnrollment saved = rampEnrollmentRepository.save(enrollment);
        return WorkflowResponse.builder()
                .status(200)
                .message("Ramp enrollment saved successfully")
                .data(saved)
                .build();
    }

    @Override
    public WorkflowResponse saveRegistration(RampRegistrationRequest request) {
        RampRegistration registration = mapRegistration(request);
        RampRegistration saved = rampRegistrationRepository.save(registration);
        return WorkflowResponse.builder()
                .status(200)
                .message("Ramp registration saved successfully")
                .data(saved)
                .build();
    }

    private RampEnrollment mapEnrollment(RampEnrollmentRequest request) {
        RampEnrollment enrollment = new RampEnrollment();
        enrollment.setRampWorkshop(request.getRampWorkshop());
        enrollment.setFirstName(request.getFirstName());
        enrollment.setLastName(request.getLastName());
        enrollment.setAge(request.getAge());
        enrollment.setGender(request.getGender());
        enrollment.setEmail(request.getEmail());
        enrollment.setPhone(request.getPhone());
        enrollment.setAadhaarNo(request.getAadhaarNo());
        enrollment.setUdhyamRegistrationNo(request.getUdhyamRegistrationNo());
        enrollment.setSocialCategory(request.getSocialCategory());
        enrollment.setMinorities(request.getMinorities());
        enrollment.setDisability(request.getDisability());
        enrollment.setDesignation(request.getDesignation());
        enrollment.setOrganization(request.getOrganization());
        enrollment.setSector(request.getSector());
        enrollment.setAddress(request.getAddress());
        enrollment.setDistrict(request.getDistrict());
        enrollment.setMandal(request.getMandal());
        return enrollment;
    }

    private RampRegistration mapRegistration(RampRegistrationRequest request) {
        RampRegistration registration = new RampRegistration();
        registration.setFirstName(request.getFirstName());
        registration.setLastName(request.getLastName());
        registration.setAge(request.getAge());
        registration.setGender(request.getGender());
        registration.setEmail(request.getEmail());
        registration.setPhoneNo(request.getPhoneNo());
        registration.setAadhaarNo(request.getAadhaarNo());
        registration.setUdhyamRegistrationNo(request.getUdhyamRegistrationNo());
        registration.setSocialCategory(request.getSocialCategory());
        registration.setMinorities(request.getMinorities());
        registration.setDisability(request.getDisability());
        registration.setDesignation(request.getDesignation());
        registration.setOrganization(request.getOrganization());
        registration.setSector(request.getSector());
        registration.setAddress(request.getAddress());

        if (request.getProgramId() != null) {
            Program program = programRepository.findById(request.getProgramId())
                    .orElseThrow(() -> new IllegalArgumentException("Program not found for id " + request.getProgramId()));
            registration.setProgram(program);
            program.setRampRegistration(registration);
        }
        return registration;
    }



    public List<DistrictProgramReport> getDistrictWiseReport() {
        return rampEnrollmentRepository.getDistrictProgramReport();
    }
}
