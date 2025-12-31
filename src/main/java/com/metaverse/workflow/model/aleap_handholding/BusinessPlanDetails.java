package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.Organization;
import com.metaverse.workflow.model.Participant;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "business_plan_details")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPlanDetails  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "business_plan_details_id")
    protected Long businessPlanDetailsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name = "plan_file_upload_path")
    private String planFileUploadPath;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "bank_remarks", length = 1000)
    private String bankRemarks;

    @ManyToMany
    @JoinTable(
            name = "business_plan_participants",
            joinColumns = @JoinColumn(name = "business_plan_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private List<Participant> participants;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name ="counselled_by")
    private String counselledBy;

    @Column(name = "counselling_date")
    private Date counsellingDate;

    @Column(name = "counselling_time")
    private String counsellingTime;

    @CreatedDate
    @Column(name = "created_timestamp", updatable = false)
    protected LocalDateTime createdTimestamp;

    @LastModifiedDate
    @Column(name = "last_modified")
    protected LocalDateTime lastModified;
}
