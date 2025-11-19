package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.BillRemarksStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "nimsme_content_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NIMSMEContentDetails extends BaseEntity {

    @Column(name = "content_type")
    private String contentType; // values: "Video" or "Document"

    @Column(name = "content_name")
    private String contentName;

    @Column(name = "duration_or_pages")
    private Integer durationOrPages;

    @Column(name = "topic")
    private String topic;

    @Column(name = "status")
    private String status; // Appr / NotAppr / Pending

    @Column(name = "date_of_upload")
    private Date dateOfUpload;

    @Column(name = "url")
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_activity_id", nullable = false)
    private NonTrainingSubActivity nonTrainingSubActivity;

    @Column(name = "bill_status")
    private BillRemarksStatus billStatus;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "nimsmeContentDetails")
    private List<NonTrainingSpiuComments> spiuComments;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "nimsmeContentDetails")
    private List<NonTrainingAgencyComments> agencyComments;

}