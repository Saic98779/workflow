package com.metaverse.workflow.programoutcometargets.service;

import com.metaverse.workflow.model.FinancialTarget;
import com.metaverse.workflow.model.PhysicalTarget;

public class TargetResponseMapper {

    public static FinancialTargetResponse mapFinancialTarget(FinancialTarget request)
    {
        return FinancialTargetResponse.builder()
                .financialTargetId(request.getFinancialTargetId())
                .financialYear(request.getFinancialYear())
                .q1(request.getQ1())
                .q2(request.getQ2())
                .q3(request.getQ3())
                .q4(request.getQ4())
                .agencyName(request.getAgency().getAgencyName())
                .outcomeTableName(request.getProgramOutcomeTable().getOutcomeTableName())
                .build();
    }
    public static PhysicalTargetResponse mapPhysicalTarget(PhysicalTarget request)
    {
        return PhysicalTargetResponse.builder()
                .physicalTargetId(request.getPhysicalTargetId())
                .outcomeId(request.getProgramOutcomeTable().getOutcomeTableId())
                .financialYear(request.getFinancialYear())
                .q1(request.getQ1())
                .q2(request.getQ2())
                .q3(request.getQ3())
                .q4(request.getQ4())
                .total(request.getQ1()+ request.getQ2()+ request.getQ3()+ request.getQ4())
                .agencyName(request.getAgency().getAgencyName())
                .outcomeTableName(request.getProgramOutcomeTable().getOutcomeTableName())
                .build();
    }

    public static TargetResponse mapPhysicalTargetResponse(PhysicalTarget target) {
        if (target == null) return null;

        return TargetResponse.builder()
                .physicalTargetId(target.getPhysicalTargetId())
                .outcomeId(target.getProgramOutcomeTable().getOutcomeTableId())
                .agencyName(target.getAgency().getAgencyName()) // assuming a nested object
                .outcomeTableName(target.getProgramOutcomeTable().getOutcomeTableName()) // assuming a nested object
                .financialYear(target.getFinancialYear())
                .q1(target.getQ1())
                .q2(target.getQ2())
                .q3(target.getQ3())
                .q4(target.getQ4())
                .total(target.getQ1()+ target.getQ2()+ target.getQ3()+ target.getQ4())
                .build();
    }
}
