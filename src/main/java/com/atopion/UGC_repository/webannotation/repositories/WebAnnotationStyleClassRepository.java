package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationStyleClassEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationStyleClassRepository extends JpaRepository<WebAnnotationStyleClassEntity, Integer> {

	@Query("select a from WebAnnotationStyleClassEntity a where (?1 is null or a.style_class_id = ?1) and (?2 is null or a.style_class like %?2%) and (?3 is null or a.bodyEntity = ?3) and (?4 is null or a.targetEntity = ?4)")
	List<WebAnnotationStyleClassEntity> findByParams(Integer style_class_id, String style_class, Integer bodyEntity, Integer targetEntity);

}