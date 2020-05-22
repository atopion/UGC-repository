package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationRenderedViaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationRenderedViaRepository extends JpaRepository<WebAnnotationRenderedViaEntity, Integer> {

	@Query("select a from WebAnnotationRenderedViaEntity a where (?1 is null or a.rendered_via_id = ?1) and (?2 is null or a.id like %?2%) and (?3 is null or a.type like %?3%) and (?4 is null or a.bodyEntity = ?4) and (?5 is null or a.targetEntity = ?5)")
	List<WebAnnotationRenderedViaEntity> findByParams(Integer rendered_via_id, String id, String type, Integer bodyEntity, Integer targetEntity);

}