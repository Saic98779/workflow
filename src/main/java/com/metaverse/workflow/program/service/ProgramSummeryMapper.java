package com.metaverse.workflow.program.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.Program;

public class ProgramSummeryMapper {
    public static ProgramSummary map(Program program) {
        return ProgramSummary.builder()
                .programName(program.getProgramTitle())
                .agencyName(program.getAgency().getAgencyName())
                .participant(program.getParticipants().size())
                .startDate(DateUtil.dateToString(program.getStartDate(), "dd-MM-yyyy"))
                .endDate(DateUtil.dateToString(program.getEndDate(), "dd-MM-yyyy"))
                .sc(program.getParticipants().stream()
                        .filter(participant -> "sc".equalsIgnoreCase(participant.getCategory()))
                        .count())

                .st(program.getParticipants().stream()
                        .filter(participant -> "st".equalsIgnoreCase(participant.getCategory()))
                        .count())

                .oc(program.getParticipants().stream()
                        .filter(participant -> "oc".equalsIgnoreCase(participant.getCategory()))
                        .count())

                .bc(program.getParticipants().stream()
                        .filter(participant -> "bc".equalsIgnoreCase(participant.getCategory()))
                        .count())

                .obc(program.getParticipants().stream()
                        .filter(participant -> "obc".equalsIgnoreCase(participant.getCategory()))
                        .count())

                .minorities(program.getParticipants().stream()
                        .filter(participant -> "minorities".equalsIgnoreCase(participant.getCategory()))
                        .count())
                .male(program.getParticipants().stream()
                        .filter(participant -> participant.getGender() != null && participant.getGender() == 'M')
                        .count())

                .female(program.getParticipants().stream()
                        .filter(participant -> participant.getGender() != null && participant.getGender() == 'F')
                        .count())

                .transgender(program.getParticipants().stream()
                        .filter(participant -> participant.getGender() != null && participant.getGender() == 'T')
                        .count())

                .physicallyChallenge(program.getParticipants().stream()
                        .filter(participant -> participant.getDisability() != null && participant.getDisability() == 'Y')
                        .count())

                .noOfSHGs(program.getParticipants().stream()
                        .filter(participant -> participant.getOrganization() != null)
                        .filter(participant -> "SHG".equalsIgnoreCase(participant.getOrganization().getOrganizationType()))
                        .count())

                .noOfMSMEs(
                        program.getParticipants().stream()
                                .filter(participant -> participant.getOrganization() != null)
                                .filter(participant -> "MSME".equalsIgnoreCase(participant.getOrganization().getOrganizationType()))
                                .count()
                )
                .noOfStartups(program.getParticipants().stream()
                        .filter(participant -> participant.getOrganization() != null)
                        .filter(participant -> "Start Up".equalsIgnoreCase(participant.getOrganization().getOrganizationType()))
                        .count())

                .noOfAspirants(
                        program.getParticipants().stream()
                                .filter(participant -> participant.getOrganization() == null)
                                .count()
                )
                .noOfDepartment(
                        program.getParticipants().stream()
                                .filter(participant -> participant.getOrganization() !=null)
                                .filter(participant -> "Department".equalsIgnoreCase(participant.getOrganization().getOrganizationType()))
                                .count()
                )
                .build();
    }
}
