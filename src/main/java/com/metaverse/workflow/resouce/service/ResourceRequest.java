package com.metaverse.workflow.resouce.service;

import com.metaverse.workflow.model.Agency;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
@Builder
public class ResourceRequest {
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
    private Set<Long> agencyIds;
    private  Boolean isVIP;
}
