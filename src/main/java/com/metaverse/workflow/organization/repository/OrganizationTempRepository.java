package com.metaverse.workflow.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metaverse.workflow.model.OrganizationTemp;

import java.util.List;

public interface OrganizationTempRepository extends JpaRepository<OrganizationTemp, Long> {
    List<OrganizationTemp> findByContactNo(Long mobileNo);

    List<OrganizationTemp> findAllByOrganizationNameIgnoreCaseAndOrganizationType(String organizationName, String organizationType);

    List<OrganizationTemp> findAllByOrderByOrganizationNameAsc();
    
    List<OrganizationTemp> findByOrganizationId(Long organizationId);
}