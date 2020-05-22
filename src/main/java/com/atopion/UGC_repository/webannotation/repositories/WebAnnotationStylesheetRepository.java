package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationStylesheetEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationStylesheetRepository extends JpaRepository<WebAnnotationStylesheetEntity, Integer> {

	@Query("select a from WebAnnotationStylesheetEntity a where (?1 is null or a.stylesheet_id = ?1) and (?2 is null or a.value like %?2%)")
	List<WebAnnotationStylesheetEntity> findByParams(Integer stylesheet_id, String value);

}