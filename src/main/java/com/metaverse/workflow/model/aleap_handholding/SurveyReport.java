package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.BaseEntity;
import com.metaverse.workflow.model.NonTrainingActivity;
import com.metaverse.workflow.model.NonTrainingSubActivity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "survey_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "survey_report_id")
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private NonTrainingActivity nonTrainingActivity;

    @Column(name = "report_submission_date")
    private Date reportSubmissionDate;

    @Column(name = "approval_date")
    private Date approvalDate;

    @Column(name = "report_file")
    private String reportFile;

    @CreatedDate
    @Column(name = "created_timestamp", updatable = false)
    protected LocalDateTime createdTimestamp;

    @LastModifiedDate
    @Column(name = "last_modified")
    protected LocalDateTime lastModified;
}
