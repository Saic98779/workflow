package com.metaverse.workflow.trainingandnontrainingtarget.service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TargetRequest   {

    private Long agencyId;
    private Long subActivityId;

    private Long q1Target;
    private Long q2Target;
    private Long q3Target;
    private Long q4Target;

    private Double q1Budget;
    private Double q2Budget;
    private Double q3Budget;
    private Double q4Budget;

    private String financialYear;

}
