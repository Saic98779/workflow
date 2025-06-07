package com.metaverse.workflow.programoutcometargets.service;

import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.FinancialTarget;
import com.metaverse.workflow.model.PhysicalTarget;
import com.metaverse.workflow.model.outcomes.ProgramOutcomeTable;

public class TargetRequestMapper {
    public static FinancialTarget mapFinancialTarget(FinancialTargetRequest request, Agency agency, ProgramOutcomeTable outcomeTable)
    {
        return FinancialTarget.builder()
                .financialYear(request.getFinancialYear())
                .q1(request.getQ1())
                .q2(request.getQ2())
                .q3(request.getQ3())
                .q4(request.getQ4())
                .agency(agency)
                .programOutcomeTable(outcomeTable)
                .build();
    }public static PhysicalTarget mapPhysicalTarget(PhysicalTargetRequest request, Agency agency, ProgramOutcomeTable outcomeTable)
    {
        return PhysicalTarget.builder()
                .financialYear(request.getFinancialYear())
                .q1(request.getQ1())
                .q2(request.getQ2())
                .q3(request.getQ3())
                .q4(request.getQ4())
                .agency(agency)
                .programOutcomeTable(outcomeTable)
                .build();
    }

    public static void mapUpdateFinancialTarget(FinancialTarget existingTarget, FinancialTargetRequest request)
    {
        existingTarget.setFinancialYear(request.getFinancialYear());
        existingTarget.setQ1(request.getQ1());
        existingTarget.setQ2(request.getQ2());
        existingTarget.setQ3(request.getQ3());
        existingTarget.setQ4(request.getQ4());

    }
    public static void mapUpdatePhysicalTarget(PhysicalTarget existingTarget, PhysicalTargetRequest request)
    {
        existingTarget.setFinancialYear(request.getFinancialYear());
        existingTarget.setQ1(request.getQ1());
        existingTarget.setQ2(request.getQ2());
        existingTarget.setQ3(request.getQ3());
        existingTarget.setQ4(request.getQ4());

    }
}
