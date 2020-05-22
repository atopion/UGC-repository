package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationAgentEmailEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationAgentEmailRepository extends JpaRepository<WebAnnotationAgentEmailEntity, Integer> {

	@Query("select a from WebAnnotationAgentEmailEntity a where (?1 is null or a.agent_email_id = ?1) and (?2 is null or a.email like %?2%) and (?3 is null or a.agentEntity = ?3)")
	List<WebAnnotationAgentEmailEntity> findByParams(Integer agent_email_id, String email, Integer agentEntity);

}