package com.metaverse.workflow.email.repository;

import com.metaverse.workflow.email.entity.EmailConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailConfigurationRepository extends JpaRepository<EmailConfiguration, Long> {
    EmailConfiguration findByAgency_AgencyId(Long agencyId);
}
