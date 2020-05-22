package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationAgentEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationAgentRepository extends JpaRepository<WebAnnotationAgentEntity, Integer> {

	@Query("select a from WebAnnotationAgentEntity a where (?1 is null or a.agent_id = ?1) and (?2 is null or a.id like %?2%) and (?3 is null or a.nickname like %?3%) and (?4 is null or a.annotationEntity = ?4) and (?5 is null or a.bodyEntity = ?5)")
	List<WebAnnotationAgentEntity> findByParams(Integer agentEntity, String id, String nickname, Integer annotationEntity, Integer bodyEntity);

}