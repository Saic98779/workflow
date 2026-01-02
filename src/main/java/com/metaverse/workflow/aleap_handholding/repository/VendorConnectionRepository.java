package com.metaverse.workflow.aleap_handholding.repository;

import com.metaverse.workflow.model.aleap_handholding.VendorConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorConnectionRepository extends JpaRepository<VendorConnection, Long> {

    List<VendorConnection>
    findByHandholdingSupport_NonTrainingSubActivity_SubActivityId(Long subActivityId);
}
