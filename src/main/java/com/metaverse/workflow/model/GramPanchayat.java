package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Gram_panchayat")
public class GramPanchayat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Gram_panchayat_id")
    private Integer gramPanchayatID;

    @Column(name ="Gram_panchayat_Name")
    private  String gramPanchayatName;

    @ManyToOne
    @JoinColumn(name = "mandal")
    @JsonIgnore
    private Mandal mandal;

    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;

    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;

}
