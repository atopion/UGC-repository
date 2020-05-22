package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationViaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationViaRepository extends JpaRepository<WebAnnotationViaEntity, Integer> {

	@Query("select a from WebAnnotationViaEntity a where (?1 is null or a.via_id = ?1) and (?2 is null or a.via like %?2%) and (?3 is null or a.annotationEntity = ?3) and (?4 is null or a.targetEntity = ?4) and (?5 is null or a.bodyEntity = ?5)")
	List<WebAnnotationViaEntity> findByParams(Integer via_id, String via, Integer annotationEntity, Integer targetEntity, Integer bodyEntity);

}