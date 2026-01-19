package com.metaverse.workflow.organization.service;

import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Sector;
import com.metaverse.workflow.organization.repository.OrganizationRepository;
import com.metaverse.workflow.sector.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository repository;
    @Autowired
    private SectorRepository sectorRepository;

    @Override
    public WorkflowResponse saveOrganization(OrganizationRequest organizationRequest) {
        List<Sector> sectors = sectorRepository.findAllById(organizationRequest.getSectorIds());
        if (sectors.size() != organizationRequest.getSectorIds().size())
            return WorkflowResponse.builder().message("Some sectors are not found").status(400).build();
        Organization organization = OrganizationRequestMapper.map(organizationRequest, sectors);
        //Organization organization = OrganizationRequestMapper.map(organizationRequest);
        Organization SavedOrganization = repository.save(organization);
        OrganizationResponse response = OrganizationResponseMapper.map(SavedOrganization);
        return WorkflowResponse.builder().message("Organization saved successfully").status(200).data(response).build();
    }

    @Override
    public Optional<Organization> getOrganizationById(Long organizationId) {
        return repository.findById(organizationId);
    }

    @Override
    @Cacheable("organizations")
    public WorkflowResponse getOrganizations() {
        List<Organization> organizationList = repository.findAllByOrderByOrganizationNameAsc();
        return WorkflowResponse.builder().message("Success").status(200).data(OrganizationResponseMapper.mapOrganization(organizationList)).build();
    }

    @Override
    @Cacheable("organizations")
    public WorkflowResponse getOrganizations(int page, int size, String orgType) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Organization> organizations;
        if (orgType == null) {
            organizations = repository.findAll(pageRequest);
        } else {
            organizations = repository.findByOrganizationType(orgType.toUpperCase(), pageRequest);
        }
        List<OrganizationResponse> organizationResponses =
                OrganizationResponseMapper.mapOrganization(organizations.getContent());

        return WorkflowResponse.builder()
                .message("Success")
                .status(200)
                .data(organizationResponses)
                .totalPages(organizations.getTotalPages())
                .totalElements(organizations.getTotalElements())
                .build();
    }


    @Override
    public Boolean isMobileNumberExists(Long mobileNo) {
        List<Organization> organization = repository.findByContactNo(mobileNo);
        if (organization.isEmpty()) return false;
        return true;
    }

    @Override
    public WorkflowResponse getAllOrganizations() {
        return WorkflowResponse.builder().message("Success").status(200).data(repository.getAllOrganizations()).build();
    }
    @Override
    public WorkflowResponse getAllOrganizations(Integer page, Integer size, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("organizationName").ascending());
        Page<OrganizationDto> result = repository.getAllOrganizations(search, pageable);

        return WorkflowResponse.builder()
                .message("Success")
                .status(200)
                .data(result.getContent())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build();
    }


    @Override
    public WorkflowResponse updateOrganization(Long organizationId, OrganizationRequest req) {
        Organization org = repository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found: " + organizationId));

        if (req.getOrganizationName() != null) org.setOrganizationName(req.getOrganizationName());
        if (req.getOrganizationCategory() != null) org.setOrganizationCategory(req.getOrganizationCategory());
        if (req.getOrganizationType() != null) org.setOrganizationType(req.getOrganizationType());
        if (req.getUdyamRegistrationNo() != null) org.setUdyamregistrationNo(req.getUdyamRegistrationNo());
        if (req.getDateOfRegistration() != null) org.setDateOfRegistration(req.getDateOfRegistration());
        if (req.getStartupCertificateNo() != null) org.setStartupCertificateNo(req.getStartupCertificateNo());
        if (req.getNatureOfStartup() != null) org.setNatureOfStartup(req.getNatureOfStartup());
        if (req.getAreasOfWorking() != null) org.setAreasOfWorking(req.getAreasOfWorking());
        if (req.getIncorporationDate() != null) org.setIncorporationDate(req.getIncorporationDate());
        if (req.getDateOfIssue() != null) org.setDateOfIssue(req.getDateOfIssue());
        if (req.getValidUpto() != null) org.setValidUpto(req.getValidUpto());
        if (req.getStateId() != null) org.setStateId(req.getStateId());
        if (req.getDistId() != null) org.setDistId(req.getDistId());
        if (req.getMandal() != null) org.setMandal(req.getMandal());
        if (req.getTown() != null) org.setTown(req.getTown());
        if (req.getStreetNo() != null) org.setStreetNo(req.getStreetNo());
        if (req.getHouseNo() != null) org.setHouseNo(req.getHouseNo());
        if (req.getLatitude() != null) org.setLatitude(req.getLatitude());
        if (req.getLongitude() != null) org.setLongitude(req.getLongitude());
        if (req.getContactNo() != null) org.setContactNo(req.getContactNo());
        if (req.getEmail() != null) org.setEmail(req.getEmail());
        if (req.getWebsite() != null) org.setWebsite(req.getWebsite());
        if (req.getOwnerName() != null) org.setOwnerName(req.getOwnerName());
        if (req.getOwnerContactNo() != null) org.setOwnerContactNo(req.getOwnerContactNo());
        if (req.getOwnerEmail() != null) org.setOwnerEmail(req.getOwnerEmail());
        if (req.getOwnerAddress() != null) org.setOwnerAddress(req.getOwnerAddress());
        if (req.getNameOfTheSHG() != null) org.setNameOfTheSHG(req.getNameOfTheSHG());
        if (req.getNameOfTheVO() != null) org.setNameOfTheVO(req.getNameOfTheVO());
        if (req.getGramaPanchayat() != null) org.setGramaPanchayat(req.getGramaPanchayat());

        Organization save = repository.save(org);
        if (save != null)
            return WorkflowResponse.builder().message("Organization Updated successfully").status(200).build();
        return WorkflowResponse.builder().message("Organization not updated successfully").status(400).build();
    }

    @Override
    public WorkflowResponse getOrganizationbyId(Long organizationId) {
        Optional<Organization> byId = repository.findById(organizationId);
        try {
            if (byId.isPresent()) {
                return WorkflowResponse.builder().message("Organization found successfully").status(200).data(OrganizationResponseMapper.map(byId.get())).build();
            } else {
                return WorkflowResponse.builder().message("Organization not found").status(404).build();
            }
        } catch (Exception e) {
            return WorkflowResponse.builder().message("Organization not found").status(404).message(e.getMessage()).build();
        }

    }
}
