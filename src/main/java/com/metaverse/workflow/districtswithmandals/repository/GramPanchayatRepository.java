package com.metaverse.workflow.districtswithmandals.repository;

import com.metaverse.workflow.model.GramPanchayat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GramPanchayatRepository extends JpaRepository<GramPanchayat,Integer> {
    List<GramPanchayat> findByMandalMandalId(Integer mandalId);

}
