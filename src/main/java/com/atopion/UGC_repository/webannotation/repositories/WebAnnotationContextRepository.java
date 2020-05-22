package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationContextEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationContextRepository extends JpaRepository<WebAnnotationContextEntity, Integer> {

	@Query("select a from WebAnnotationContextEntity a where (?1 is null or a.context_id = ?1) and (?2 is null or a.context like %?2%) and (?3 is null or a.annotationEntity = ?3)")
	List<WebAnnotationContextEntity> findByParams(Integer context_id, String context, Integer annotationEntity);

}