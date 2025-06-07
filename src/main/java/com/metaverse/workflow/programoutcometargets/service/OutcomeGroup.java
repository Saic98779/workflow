package com.metaverse.workflow.programoutcometargets.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutcomeGroup {
    private String outcomeTableName;
    private long grandTotal = 0;
    private Set<String> financialYearHeaders;
    private List<TargetResponse> financialYear = new ArrayList<>();

    public void addTargetResponse(TargetResponse response) {
        financialYear.add(response);
        if (response.getTotal() != null) {
            grandTotal += response.getTotal();
        }
    }
}
