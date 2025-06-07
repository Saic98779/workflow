package com.metaverse.workflow.resouce.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.metaverse.workflow.model.Agency;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceResponse {

	 private Long resourceId;
	    private String name;
	    private Character gender;
	    private Long mobileNo;
	    private String organizationName;
	    private String email;
	    private String designation;
	    private String qualification;
	    private String specialization;
	    private String briefDescription;
	    private List<String> agencyNames;
	    private List<Agency> agency;
		private Boolean isVIP;
}
