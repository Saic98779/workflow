package com.metaverse.workflow.resouce.service;

import com.metaverse.workflow.model.Agency;
import org.springframework.stereotype.Component;

import com.metaverse.workflow.model.Resource;

@Component
public class ResourceResponseMapper {

	public static ResourceResponse map(Resource request)
	{
		return ResourceResponse.builder()
				.resourceId(request.getResourceId())
				.name(request.getName())
				.gender(request.getGender())
				.mobileNo(request.getMobileNo())
				.organizationName(request.getOrganizationName())
				.email(request.getEmail())
				.designation(request.getDesignation())
				.qualification(request.getQualification())
				.specialization(request.getSpecialization())
				.briefDescription(request.getBriefDescription())
				.agencyNames(request.getAgency().stream().map(Agency::getAgencyName).toList())
				.isVIP(request.getIsVIP())
				.build();
	}
}
