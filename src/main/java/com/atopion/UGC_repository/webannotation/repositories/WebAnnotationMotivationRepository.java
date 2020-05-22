package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationMotivationEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationMotivationRepository extends JpaRepository<WebAnnotationMotivationEntity, Integer> {

	@Query("select a from WebAnnotationMotivationEntity a where (?1 is null or a.motivation_id = ?1) and (?2 is null or a.motivation like %?2%) and (?3 is null or a.annotationEntity = ?3)")
	List<WebAnnotationMotivationEntity> findByParams(Integer motivation_id, String motivation, Integer annotationEntity);

}