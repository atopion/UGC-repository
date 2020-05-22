package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationAgentNameEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationAgentNameRepository extends JpaRepository<WebAnnotationAgentNameEntity, Integer> {

	@Query("select a from WebAnnotationAgentNameEntity a where (?1 is null or a.agent_name_id = ?1) and (?2 is null or a.name like %?2%) and (?3 is null or a.agentEntity = ?3)")
	List<WebAnnotationAgentNameEntity> findByParams(Integer agent_name_id, String name, Integer agentEntity);

}