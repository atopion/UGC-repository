package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationEmailSha1Entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationEmailSha1Repository extends JpaRepository<WebAnnotationEmailSha1Entity, Integer> {

	@Query("select a from WebAnnotationEmailSha1Entity a where (?1 is null or a.agent_email_sha1_id = ?1) and (?2 is null or a.email_sha1 like %?2%) and (?3 is null or a.agentEntity = ?3)")
	List<WebAnnotationEmailSha1Entity> findByParams(Integer agent_email_sha1_id, String email_sha1, Integer agentEntity);

}