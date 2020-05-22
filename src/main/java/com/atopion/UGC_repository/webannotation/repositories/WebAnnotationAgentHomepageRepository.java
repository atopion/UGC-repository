package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationAgentHomepageEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationAgentHomepageRepository extends JpaRepository<WebAnnotationAgentHomepageEntity, Integer> {

	@Query("select a from WebAnnotationAgentHomepageEntity a where (?1 is null or a.agent_homepage_id = ?1) and (?2 is null or a.homepage like %?2%) and (?3 is null or a.agentEntity = ?3)")
	List<WebAnnotationAgentHomepageEntity> findByParams(Integer agent_homepage_id, String homepage, Integer agentEntity);

}