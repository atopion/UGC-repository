package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationRightsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationRightsRepository extends JpaRepository<WebAnnotationRightsEntity, Integer> {

	@Query("select a from WebAnnotationRightsEntity a where (?1 is null or a.rights_id = ?1) and (?2 is null or a.rights like %?2%) and (?3 is null or a.annotationEntity = ?3) and (?4 is null or a.bodyEntity = ?4) and (?5 is null or a.targetEntity = ?5)")
	List<WebAnnotationRightsEntity> findByParams(Integer rights_id, String rights, Integer annotationEntity, Integer bodyEntity, Integer targetEntity);

}