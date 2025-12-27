package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.CounsellorRegistration;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "business_plan_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPlanDetails extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name = "business_plan_date")
    private LocalDate businessPlanDate;

    @Column(name = "plan_file_upload_path")
    private String planFileUploadPath;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "bank_remarks", length = 1000)
    private String bankRemarks;
}
