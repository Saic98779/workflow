package com.metaverse.workflow.email.entity;

import com.metaverse.workflow.email.StringListJsonConverter;
import com.metaverse.workflow.model.Agency;
import com.metaverse.workflow.model.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "email_configuration")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EmailConfiguration extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    @Convert(converter = StringListJsonConverter.class)
    @Column(columnDefinition = "json")
    private List<String> operationalToEmailAddresses;

    @Convert(converter = StringListJsonConverter.class)
    @Column(columnDefinition = "json")
    private List<String> operationalCcEmailAddresses;

    @Convert(converter = StringListJsonConverter.class)
    @Column(columnDefinition = "json")
    private List<String> financeToEmailAddresses;

    @Convert(converter = StringListJsonConverter.class)
    @Column(columnDefinition = "json")
    private List<String> financeCcEmailAddresses;
}

