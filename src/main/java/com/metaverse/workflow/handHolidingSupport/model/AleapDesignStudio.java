package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "aleap_design_studio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AleapDesignStudio extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packaging_branding_support_id", nullable = false)
    private PackagingBrandingSupport packagingBrandingSupport;

    @Column(name = "studio_access_date")
    private LocalDate studioAccessDate;

    @Column(name = "details", length = 1000)
    private String details;
}
