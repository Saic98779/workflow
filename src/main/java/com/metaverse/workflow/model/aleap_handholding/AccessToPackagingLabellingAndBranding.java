package com.metaverse.workflow.model.aleap_handholding;

import com.metaverse.workflow.model.Organization;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Table(name = "access_to_packaging_labelling_and_branding")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessToPackagingLabellingAndBranding {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="access_to_packaging_id")
    private Long accessToPackagingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handholding_support_id", nullable = false)
    private HandholdingSupport handholdingSupport;

    @Column(name="access_to_packaging_type")
    private String accessToPackagingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "studio_access_date")
    private Date studioAccessDate;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "aleap_design_studio_image1")
    private String aleapDesignStudioImage1;

    @Column(name = "aleap_design_studio_image2")
    private String aleapDesignStudioImage2;

    @Column(name = "aleap_design_studio_image3")
    private String aleapDesignStudioImage3;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private EventType eventType;

    @Column(name = "event_date")
    private Date eventDate;

    @Column(name = "event_location")
    private String eventLocation;

    @Column(name = "organized_by")
    private String organizedBy;

    @CreatedDate
    @Column(name = "created_timestamp", updatable = false)
    protected LocalDateTime createdTimestamp;

    @LastModifiedDate
    @Column(name = "last_modified")
    protected LocalDateTime lastModified;

    public enum EventType {
        TRADE_FAIR,
        EXHIBITION,
        BUYER_SELLER_MEET
    }
}
