package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationLanguageEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationLanguageRepository extends JpaRepository<WebAnnotationLanguageEntity, Integer> {

	@Query("select a from WebAnnotationLanguageEntity a where (?1 is null or a.language_id = ?1) and (?2 is null or a.language like %?2%) and (?3 is null or a.bodyEntity = ?3) and (?4 is null or a.targetEntity = ?4)")
	List<WebAnnotationLanguageEntity> findByParams(Integer language_id, String language, Integer bodyEntity, Integer targetEntity);

}