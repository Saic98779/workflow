package com.metaverse.workflow.organization.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.location.service.LocationResponse;
import com.metaverse.workflow.model.Location;
import org.springframework.stereotype.Component;

import com.metaverse.workflow.model.Organization;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class OrganizationResponseMapper {

	public static OrganizationResponse map(Organization request)
	{
		List<OrganizationResponse.Sector> sectors = request.getSectors().stream().map(sector -> OrganizationResponse.Sector.builder().sectorId(sector.getSectorId()).sectorName(sector.getSectorName()).build()).toList();
		return OrganizationResponse.builder().organizationId(request.getOrganizationId())
				.organizationName(request.getOrganizationName())
				.organizationCategory(request.getOrganizationCategory())
				.organizationType(request.getOrganizationType())
				.udyamregistrationNo(request.getUdyamregistrationNo())
				.dateOfRegistration(DateUtil.dateToString(request.getDateOfRegistration(),"dd-MM-yyyy"))
				.startupCertificateNo(request.getStartupCertificateNo())
				.natureOfStartup(request.getNatureOfStartup())
				.areasOfWorking(request.getAreasOfWorking())
				.incorporationDate(DateUtil.dateToString(request.getIncorporationDate(),"dd-MM-yyyy"))
				.dateOfIssue(request.getDateOfIssue())
				.validUpto(request.getValidUpto())
				.stateId(request.getStateId())
				.distId(request.getDistId())
				.mandal(request.getMandal())
				.town(request.getTown())
				.streetNo(request.getStreetNo())
				.houseNo(request.getHouseNo())
				.latitude(request.getLatitude())
				.longitude(request.getLongitude())
				.contactNo(request.getContactNo())
				.email(request.getEmail())
				.email(request.getEmail())
				.website(request.getWebsite())
				.ownerName(request.getOwnerName())
				.ownerContactNo(request.getOwnerContactNo())
				.ownerEmail(request.getOwnerEmail())
				.ownerAddress(request.getOwnerAddress())
				.nameOfTheSHG(request.getNameOfTheSHG())
				.nameOfTheVO(request.getNameOfTheVO())
				.gramaPanchayat(request.getGramaPanchayat()).nameOfTheSHG(request.getNameOfTheSHG()).nameOfTheVO(request.getNameOfTheVO()).gramaPanchayat(request.getGramaPanchayat())
				.sectorList(sectors).build();
	}

	public static List<OrganizationResponse> mapOrganization(List<Organization> organizationList) {
		return organizationList != null ? organizationList.stream().map(organization ->
				OrganizationResponse.builder()
						.organizationId(organization.getOrganizationId())
						.organizationName(organization.getOrganizationName())
						.organizationCategory(organization.getOrganizationCategory())
						.organizationType(organization.getOrganizationType())
						.udyamregistrationNo(organization.getUdyamregistrationNo())
						.dateOfRegistration(DateUtil.dateToString(organization.getDateOfRegistration(),"dd-MM-yyyy"))
						.startupCertificateNo(organization.getStartupCertificateNo())
						.natureOfStartup(organization.getNatureOfStartup())
						.areasOfWorking(organization.getAreasOfWorking())
						.incorporationDate(DateUtil.dateToString(organization.getIncorporationDate(),"dd-MM-yyyy"))
						.dateOfIssue(organization.getDateOfIssue())
						.validUpto(organization.getValidUpto())
						.stateId(organization.getStateId())
						.distId(organization.getDistId())
						.mandal(organization.getMandal())
						.town(organization.getTown())
						.streetNo(organization.getStreetNo())
						.houseNo(organization.getHouseNo())
						.latitude(organization.getLatitude())
						.longitude(organization.getLongitude())
						.contactNo(organization.getContactNo())
						.email(organization.getEmail())
						.email(organization.getEmail())
						.website(organization.getWebsite())
						.ownerName(organization.getOwnerName())
						.ownerContactNo(organization.getOwnerContactNo())
						.ownerEmail(organization.getOwnerEmail())
						.ownerAddress(organization.getOwnerAddress())
						.nameOfTheSHG(organization.getNameOfTheSHG())
						.nameOfTheVO(organization.getNameOfTheVO())
						.gramaPanchayat(organization.getGramaPanchayat())
						.build()).collect(Collectors.toList()) : null;
	}

}
