package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationTargetEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationTargetRepository extends JpaRepository<WebAnnotationTargetEntity, Integer> {

	@Query("select a from WebAnnotationTargetEntity a where (?1 is null or a.target_id = ?1) and (?2 is null or a.id like %?2%) and (?3 is null or a.value like %?3%) and (?4 is null or a.processing_language like %?4%) and (?5 is null or a.text_direction like %?5%) and (?6 is null or a.canonical like %?6%) and (?7 is null or a.source like %?7%) and (?8 is null or a.annotationEntity = ?8)")
	List<WebAnnotationTargetEntity> findByParams(Integer target_id, String id, String value, String processing_language, String text_direction, String canonical, String source, Integer annotationEntity);

}