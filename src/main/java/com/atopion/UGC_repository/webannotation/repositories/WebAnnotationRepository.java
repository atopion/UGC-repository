package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Date;

public interface WebAnnotationRepository extends JpaRepository<WebAnnotationEntity, Integer> {

	@Query("select a from WebAnnotationEntity a where (?1 is null or a.web_annotation_id = ?1) and (?2 is null or a.id like %?2%) and (?3 is null or a.created = ?3) and (?4 is null or a.modified = ?4) and (?5 is null or a.generated = ?5) and (?6 is null or a.bodyValue like %?6%) and (?7 is null or a.canonical like %?7%) and (?8 is null or a.stylesheet = ?8)")
	List<WebAnnotationEntity> findByParams(Integer web_annotationEntity, String id, Date created, Date modified, Date generated, String bodyValue, String canonical, Integer stylesheet_id);

	@Transactional
	@Modifying(flushAutomatically = true)
	@Query(value = "update WebAnnotationEntity e set e.id = :newid where e.web_annotation_id = :annoID")
	void updateIDOfWebAnnotation(@Param("annoID") int annoID, @Param("newid") String newid);
}