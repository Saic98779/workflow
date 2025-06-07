package com.metaverse.workflow.resouce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.metaverse.workflow.model.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Long>{
}
