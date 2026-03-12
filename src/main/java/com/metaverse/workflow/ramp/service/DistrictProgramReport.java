package com.metaverse.workflow.ramp.service;

public interface DistrictProgramReport {

    String getDistrictName();

    Long getTotalPrograms();
    Long getProgramsScheduled();
    Long getTotalProgramsCompleted();

    Long getParticipants();

    Long getMale();
    Long getFemale();
    Long getTrans();

    Long getSc();
    Long getSt();
    Long getBc();
    Long getMinority();
    Long getOc();

    Long getDisabled();

    Long getShgs();
    Long getShgts();
    Long getMsmes();
    Long getStartups();
    Long getOthers();
}