package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationAudienceEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationAudienceRepository extends JpaRepository<WebAnnotationAudienceEntity, Integer> {

	@Query("select a from WebAnnotationAudienceEntity a where (?1 is null or a.audience_id = ?1) and (?2 is null or a.id like %?2%) and (?3 is null or a.annotationEntity = ?3)")
	List<WebAnnotationAudienceEntity> findByParams(Integer audience_id, String id, Integer annotationEntity);

}