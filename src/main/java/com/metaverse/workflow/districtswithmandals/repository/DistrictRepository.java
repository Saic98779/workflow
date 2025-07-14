package com.metaverse.workflow.districtswithmandals.repository;

import com.metaverse.workflow.model.District;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District,Integer> {
    List<District> findByDistrictName(String district);
}
