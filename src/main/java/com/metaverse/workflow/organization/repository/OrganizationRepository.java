package com.metaverse.workflow.organization.repository;

import com.metaverse.workflow.organization.service.OrganizationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.metaverse.workflow.model.Organization;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long>{
    List<Organization> findByContactNo(Long mobileNo);

    List<Organization> findAllByOrganizationNameIgnoreCaseAndOrganizationType(String organizationName, String organizationType);

    List<Organization> findAllByOrderByOrganizationNameAsc();

    @Query("SELECT new com.metaverse.workflow.organization.service.OrganizationDto(o.organizationId, o.organizationName) FROM Organization o")
    List<OrganizationDto> getAllOrganizations();

    Page<Organization> findByOrganizationType(String orgType, PageRequest pageRequest);
}

