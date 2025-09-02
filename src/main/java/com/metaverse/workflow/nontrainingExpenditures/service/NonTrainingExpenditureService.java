package com.metaverse.workflow.nontrainingExpenditures.service;

import com.metaverse.workflow.agency.repository.AgencyRepository;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.model.*;
import com.metaverse.workflow.nontraining.repository.NonTrainingActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingExpenditureRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingResourceExpenditureRepo;
import com.metaverse.workflow.nontrainingExpenditures.repository.NonTrainingSubActivityRepository;
import com.metaverse.workflow.nontrainingExpenditures.repository.ResourceRepo;
import com.metaverse.workflow.program.repository.ProgramSessionFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NonTrainingExpenditureService {

    private final NonTrainingExpenditureRepository repository;
    private final AgencyRepository agencyRepository;
    private final NonTrainingSubActivityRepository nonTrainingSubActivityRepository;
    private final ResourceRepo resourceRepo;
    private final NonTrainingResourceExpenditureRepo resourceExpenditureRepo;
    private final StorageService storageService;
    private final ProgramSessionFileRepository programSessionFileRepository;
    private final NonTrainingActivityRepository nonTrainingActivityRepository;

    public WorkflowResponse create(NonTrainingExpenditureDTO dto, MultipartFile file) throws DataException {
        Agency agency = agencyRepository.findById(dto.getAgencyId())
                .orElseThrow(() -> new DataException("Agency not found","AGENCY_NOT_FOUND",400));
        NonTrainingSubActivity subActivity = nonTrainingSubActivityRepository.findById(dto.getNonTrainingSubActivityId())
                .orElseThrow(() -> new DataException("Activity not found","ACTIVITY_NOT_FOUND",400));

        NonTrainingActivity activity = nonTrainingActivityRepository.findById(dto.getNonTrainingSubActivityId())
                .orElseThrow(() -> new DataException("Activity not found","ACTIVITY_NOT_FOUND",400));


        NonTrainingExpenditure entity = NonTrainingExpenditureMapper.toEntity(dto, agency, activity, subActivity);
        NonTrainingExpenditure save = repository.save(entity);

        if (file != null && !file.isEmpty()) {
            String filePath = this.storageFiles(file, save.getId(), "TravelAndTransport");
            save.setUploadBillUrl(filePath);
            repository.save(save);
            programSessionFileRepository.save(ProgramSessionFile.builder()
                    .fileType("File")
                    .filePath(filePath)
                    .nonTrainingExpenditure(save)
                    .build());
        }

        return WorkflowResponse.builder()
                .status(200)
                .message("Success")
                .data(NonTrainingExpenditureMapper.toDTO(save)).build();
    }

    public List<NonTrainingExpenditureDTO> getAll() {
        return repository.findAll().stream()
                .map(NonTrainingExpenditureMapper::toDTO)
                .collect(Collectors.toList());
    }

    public NonTrainingExpenditureDTO getById(Long id) throws DataException {
        return repository.findById(id)
                .map(NonTrainingExpenditureMapper::toDTO)
                .orElseThrow(() -> new DataException("Expenditure not found","EXPENDITURE_NOT_FOUND",400));
    }


    public NonTrainingExpenditureDTO update(Long id, NonTrainingExpenditureDTO dto) throws DataException {
        NonTrainingExpenditure existing = repository.findById(id)
                .orElseThrow(() -> new DataException("Expenditure not found","EXPENDITURE_NOT_FOUND",400));

        Agency agency = agencyRepository.findById(dto.getAgencyId())
                .orElseThrow(() -> new DataException("Agency not found","AGENCY_NOT_FOUND",400));
        NonTrainingSubActivity subActivity = nonTrainingSubActivityRepository.findById(dto.getNonTrainingSubActivityId())
                .orElseThrow(() -> new DataException("Activity not found","ACTIVITY_NOT_FOUND",400));

        NonTrainingActivity activity = nonTrainingActivityRepository.findById(dto.getNonTrainingSubActivityId())
                .orElseThrow(() -> new DataException("Activity not found","ACTIVITY_NOT_FOUND",400));


        NonTrainingExpenditure updated = NonTrainingExpenditureMapper.toEntity(dto, agency, activity,  subActivity);
        updated.setId(existing.getId());
        return NonTrainingExpenditureMapper.toDTO(repository.save(updated));
    }

    public void delete(Long id) throws DataException {
        NonTrainingExpenditure existing = repository.findById(id)
                .orElseThrow(() -> new DataException("Expenditure not found","EXPENDITURE_NOT_FOUND",400));
        repository.deleteById(id);
    }
    public WorkflowResponse saveResource(NonTrainingResourceDTO resource) throws DataException {
        NonTrainingSubActivity activity = nonTrainingSubActivityRepository.findById(resource.getNonTrainingActivityId())
                .orElseThrow(() -> new DataException("Activity not found","ACTIVITY_NOT_FOUND",400));

        NonTrainingResource nonTrainingResource = NonTrainingExpenditureMapper.mapToResource(resource,activity);
        return WorkflowResponse.builder()
                .message("success")
                .status(200)
                .data(resourceRepo.save(nonTrainingResource))
                .build();
    }

    public WorkflowResponse updateResource(Long id, NonTrainingResourceDTO resourceDto) {
        if (resourceDto == null) {
            return WorkflowResponse.builder()
                    .status(400).message("Invalid request: Resource data is null").data(null).build();
        }
        NonTrainingResource existing = resourceRepo.findById(id).orElse(null);
        if (existing == null) {
            return WorkflowResponse.builder().status(404).message("Resource not found with id " + id).data(null).build();
        }

        existing.setName(resourceDto.getName() != null ? resourceDto.getName() : existing.getName());
        existing.setDesignation(resourceDto.getDesignation() != null ? resourceDto.getDesignation() : existing.getDesignation());
        existing.setRelevantExperience(resourceDto.getRelevantExperience() != null ? resourceDto.getRelevantExperience() : existing.getRelevantExperience());
        existing.setEducationalQualifications(resourceDto.getEducationalQualification() != null ? resourceDto.getEducationalQualification() : existing.getEducationalQualifications());
        existing.setDateOfJoining(
                resourceDto.getDateOfJoining() != null
                        ? DateUtil.stringToDate(resourceDto.getDateOfJoining(), "dd-MM-yyyy")
                        : existing.getDateOfJoining());
        existing.setMonthlySal(resourceDto.getMonthlySal() != null ? resourceDto.getMonthlySal() : existing.getMonthlySal());
        existing.setBankName(resourceDto.getBankName() != null ? resourceDto.getBankName() : existing.getBankName());
        existing.setIfscCode(resourceDto.getIfscCode() != null ? resourceDto.getIfscCode() : existing.getIfscCode());
        existing.setAccountNo(resourceDto.getAccountNo() != null ? resourceDto.getAccountNo() : existing.getAccountNo());

        NonTrainingResource updated = resourceRepo.save(existing);

        return WorkflowResponse.builder()
                .status(200)
                .message("Resource updated successfully")
                .data(NonTrainingExpenditureMapper.mapToResourceRes(updated))
                .build();
    }

    public WorkflowResponse deleteResource(Long id) throws DataException {
        NonTrainingResource resource = resourceRepo.findById(id)
                .orElseThrow(() -> new DataException("Resource not found with id " + id,"RESOURCE_NOT_FOUND",400));
        resourceRepo.delete(resource);
        return WorkflowResponse.builder()
                .status(200)
                .message("Resource deleted successfully")
                .build();
    }

    public WorkflowResponse saveResourceExpenditure(NonTrainingResourceExpenditureDTO resourceExpenditureDTO) throws DataException {
        NonTrainingResource nonTrainingResource = resourceRepo.findById(resourceExpenditureDTO.getResourceId())
                .orElseThrow(() -> new DataException("Resource not found with id " + resourceExpenditureDTO.getResourceId(),"RESOURCE_NOT_FOUND",400));
        NonTrainingResourceExpenditure expenditure=NonTrainingExpenditureMapper.mapToResourceExpenditure(resourceExpenditureDTO,nonTrainingResource);
        return WorkflowResponse.builder()
                .message("success")
                .status(200)
                .data(NonTrainingExpenditureMapper.mapToResourceExpenditureResponse(resourceExpenditureRepo.save(expenditure)))
                .build();
    }


    public WorkflowResponse updateResourceExpenditure(Long expenditureId, NonTrainingResourceExpenditureDTO resourceExpenditureDTO) throws DataException {
        NonTrainingResourceExpenditure existingExpenditure = resourceExpenditureRepo.findById(expenditureId)
                .orElseThrow(() -> new DataException("Expenditure not found with id " + expenditureId,"EXPENDITURE_NOT_FOUND",400));

        existingExpenditure.setAmount(
                resourceExpenditureDTO.getAmount() != null ? resourceExpenditureDTO.getAmount() : existingExpenditure.getAmount()
        );

        existingExpenditure.setPaymentForMonth(
                resourceExpenditureDTO.getPaymentForMonth() != null && !resourceExpenditureDTO.getPaymentForMonth().isEmpty()
                        ? resourceExpenditureDTO.getPaymentForMonth()
                        : existingExpenditure.getPaymentForMonth()
        );

        existingExpenditure.setDateOfPayment(
                resourceExpenditureDTO.getDateOfPayment() != null && !resourceExpenditureDTO.getDateOfPayment().isEmpty()
                        ? DateUtil.stringToDate(resourceExpenditureDTO.getDateOfPayment(), "dd-MM-yyyy")
                        : existingExpenditure.getDateOfPayment()
        );
        NonTrainingResourceExpenditure updatedExpenditure = resourceExpenditureRepo.save(existingExpenditure);

        return WorkflowResponse.builder()
                .status(200)
                .message("Expenditure updated successfully")
                .data(NonTrainingExpenditureMapper.mapToResourceExpenditureResponse(updatedExpenditure))
                .build();
    }


    public WorkflowResponse deleteResourceExpenditure(Long expenditureId) throws DataException {
        NonTrainingResourceExpenditure existingExpenditure = resourceExpenditureRepo.findById(expenditureId)
                .orElseThrow(() -> new DataException("Expenditure not found with id " + expenditureId,"EXPENDITURE_NOT_FOUND",400));

        resourceExpenditureRepo.delete(existingExpenditure);

        return WorkflowResponse.builder()
                .status(200)
                .message("Expenditure deleted successfully with id " + expenditureId)
                .build();
    }

    public WorkflowResponse getResourceByNonTrainingActivity(Long nonTrainingActivityId) throws DataException {
        List<NonTrainingResource> resourceList =  new ArrayList<>();
//        List<NonTrainingResource> resourceList=resourceRepo.findByNonTrainingActivity_ActivityId(nonTrainingActivityId)
//                .orElseThrow(() -> new DataException("Resource not found with this activity id " + nonTrainingActivityId,"RESOURCE_NOT_FOUND",400));
        return WorkflowResponse.builder().status(200)
                .message("success")
                .data(resourceList.stream().map(NonTrainingExpenditureMapper::mapToResourceResForDropdown).toList())
                .build();
    }

    public WorkflowResponse getAllResourceByNonTrainingActivityId(Long nonTrainingActivityId) throws DataException {
        List<NonTrainingResource> resourceList =  new ArrayList<>();
//        List<NonTrainingResource> resourceList=resourceRepo.findByNonTrainingActivity_ActivityId(nonTrainingActivityId)
//                .orElseThrow(() -> new DataException("Resource not found with this activity id " + nonTrainingActivityId,"RESOURCE_NOT_FOUND",400));
        return WorkflowResponse.builder().status(200)
                .message("success")
                .data(resourceList.stream().map(NonTrainingExpenditureMapper::mapToResourceRes).toList())
                .build();
    }

    public WorkflowResponse getAllExpenditureByNonTrainingActivityId(Long nonTrainingActivityId) throws DataException {
        List<NonTrainingExpenditure> expenditureList = new ArrayList<>();
//        List<NonTrainingExpenditure> expenditureList=repository.findByNonTrainingActivity_ActivityId(nonTrainingActivityId)
//                .orElseThrow(() -> new DataException("Expenditure not found with this activity id " + nonTrainingActivityId,"EXPENDITURE_NOT_FOUND",400));
        return WorkflowResponse.builder().status(200)
                .message("success")
                .data(expenditureList.stream().map(NonTrainingExpenditureMapper::toDTO).toList())
                .build();
    }
    public WorkflowResponse getAllResourceExpenditureByNonTrainingActivityId(Long nonTrainingActivityId) throws DataException {
        List<NonTrainingResource> resourceList = new ArrayList<>();
//        List<NonTrainingResource> resourceList = resourceRepo.findByNonTrainingActivity_ActivityId(nonTrainingActivityId)
//                .orElseThrow(() -> new DataException(
//                        "Resource not found with this activity id " + nonTrainingActivityId,
//                        "RESOURCE_NOT_FOUND",
//                        400));

        List<NonTrainingResourceExpenditureDTO> allExpendituresDto =
                resourceList.stream()
                        .flatMap(resource -> resource.getNonTrainingResourceExpenditures().stream())
                        .map(NonTrainingExpenditureMapper::mapToResourceExpenditureResponse) // <-- convert entity → DTO
                        .collect(Collectors.toList());

        return WorkflowResponse.builder()
                .status(200)
                .message("success")
                .data(allExpendituresDto)
                .build();
    }
    public String storageFiles(MultipartFile file, Long TravelAndTransportId, String folderName) {
        String filePath = storageService.travelAndTransportStore(file, TravelAndTransportId, folderName);
        return  filePath;
    }


}

