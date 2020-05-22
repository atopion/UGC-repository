package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationBodyEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationBodyRepository extends JpaRepository<WebAnnotationBodyEntity, Integer> {

	@Query("select a from WebAnnotationBodyEntity a where (?1 is null or a.body_id = ?1) and (?2 is null or a.id like %?2%) and (?3 is null or a.processing_language like %?3%) and (?4 is null or a.text_direction like %?4%) and (?5 is null or a.value like %?5%) and (?6 is null or a.modified = ?6) and (?7 is null or a.created = ?7) and (?8 is null or a.canonical like %?8%) and (?9 is null or a.source like %?9%) and (?10 is null or a.annotationEntity = ?10) and (?11 is null or a.choiceEntity = ?11)")
	List<WebAnnotationBodyEntity> findByParams(Integer bodyEntity, String id, String processing_language, String text_direction, String value, Date modified, Date created, String canonical, String source, Integer annotationEntity, Integer choice_id);

}