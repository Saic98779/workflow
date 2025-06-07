package com.metaverse.workflow.resouce.service;

import com.metaverse.workflow.model.Agency;
import org.springframework.stereotype.Component;

import com.metaverse.workflow.model.Resource;

import java.util.List;

@Component
public class ResourceRequestMapper {

	public static Resource map(ResourceRequest request, List<Agency> agencies)
	{
		return Resource.builder()
				.name(request.getName())
				.gender(request.getGender())
				.mobileNo(request.getMobileNo())
				.organizationName(request.getOrganizationName())
				.email(request.getEmail())
				.designation(request.getDesignation())
				.qualification(request.getQualification())
				.specialization(request.getSpecialization())
				.briefDescription(request.getBriefDescription())
				.agency(agencies)
				.isVIP(request.getIsVIP())
				.build();
	}
}
