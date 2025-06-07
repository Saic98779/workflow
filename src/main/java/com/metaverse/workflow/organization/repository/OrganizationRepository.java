package com.metaverse.workflow.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metaverse.workflow.model.Organization;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long>{
    List<Organization> findByContactNo(Long mobileNo);

    List<Organization> findAllByOrganizationNameIgnoreCaseAndOrganizationType(String organizationName, String organizationType);

    List<Organization> findAllByOrderByOrganizationNameAsc();
}
