package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationTypeEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationTypeRepository extends JpaRepository<WebAnnotationTypeEntity, Integer> {

	@Query("select a from WebAnnotationTypeEntity a where (?1 is null or a.type_id = ?1) and (?2 is null or a.type like %?2%) and (?3 is null or a.annotationEntity = ?3) and (?4 is null or a.bodyEntity = ?4) and (?5 is null or a.targetEntity = ?5) and (?6 is null or a.agentEntity = ?6) and (?7 is null or a.audienceEntity = ?7) and (?8 is null or a.stylesheetEntity = ?8) and (?9 is null or a.selectorEntity = ?9)")
	List<WebAnnotationTypeEntity> findByParams(Integer type_id, String type, Integer annotationEntity, Integer bodyEntity, Integer targetEntity, Integer agentEntity, Integer audienceEntity, Integer stylesheet_id, Integer selector_id);

}