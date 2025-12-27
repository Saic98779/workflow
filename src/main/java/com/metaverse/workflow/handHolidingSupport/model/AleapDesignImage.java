package com.metaverse.workflow.handHolidingSupport.model;

import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "aleap_design_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AleapDesignImage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aleap_design_studio_id", nullable = false)
    private AleapDesignStudio aleapDesignStudio;

    @Column(name = "image_path", nullable = false)
    private String imagePath;
}

