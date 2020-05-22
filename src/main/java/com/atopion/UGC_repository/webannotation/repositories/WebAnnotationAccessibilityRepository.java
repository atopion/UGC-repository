package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationAccessibilityEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationAccessibilityRepository extends JpaRepository<WebAnnotationAccessibilityEntity, Integer> {

	@Query("select a from WebAnnotationAccessibilityEntity a where (?1 is null or a.accessibility_id = ?1) and (?2 is null or a.accessibility like %?2%) and (?3 is null or a.targetEntity = ?3) and (?4 is null or a.bodyEntity = ?4)")
	List<WebAnnotationAccessibilityEntity> findByParams(Integer accessibility_id, String accessibility, Integer targetEntity, Integer bodyEntity);

}