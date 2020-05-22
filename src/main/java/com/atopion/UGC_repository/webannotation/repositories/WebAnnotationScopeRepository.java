package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationScopeEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationScopeRepository extends JpaRepository<WebAnnotationScopeEntity, Integer> {

	@Query("select a from WebAnnotationScopeEntity a where (?1 is null or a.scope_id = ?1) and (?2 is null or a.id like %?2%) and (?3 is null or a.bodyEntity = ?3) and (?4 is null or a.targetEntity = ?4)")
	List<WebAnnotationScopeEntity> findByParams(Integer scope_id, String id, Integer bodyEntity, Integer targetEntity);

}