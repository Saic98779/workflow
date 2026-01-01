package com.metaverse.workflow.model.aleap_handholding;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;

@Builder
@Entity
@Table(name = "aleap_design_studio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AleapDesignStudio {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="aleap_design_studio_id")
    protected Long aleapDesignStudioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name = "studio_access_date")
    private Date studioAccessDate;

    @Column(name = "details", length = 1000)
    private String details;

    @Column(name = "aleap_design_studio_image1")
    private String aleapDesignStudioImage1;

    @Column(name = "aleap_design_studio_image2")
    private String aleapDesignStudioImage2;

    @Column(name = "aleap_design_studio_image3")
    private String aleapDesignStudioImage3;

    @CreatedDate
    @Column(name = "created_timestamp", updatable = false)
    protected LocalDateTime createdTimestamp;

    @LastModifiedDate
    @Column(name = "last_modified")
    protected LocalDateTime lastModified;
}
