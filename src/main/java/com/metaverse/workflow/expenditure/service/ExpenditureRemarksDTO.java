package com.metaverse.workflow.expenditure.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenditureRemarksDTO {

    private Long id;

    private String userId;

    private String remark;

    private String remarkDate;

    private String remarkTime;

    private Long expenditureId;
}

