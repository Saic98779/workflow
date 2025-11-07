package com.metaverse.workflow.organization.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrganizationDto {
    private Long organizationId;
    private String organizationName;
}
