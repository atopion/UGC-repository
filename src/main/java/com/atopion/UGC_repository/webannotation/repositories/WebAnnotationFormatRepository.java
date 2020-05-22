package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationFormatEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationFormatRepository extends JpaRepository<WebAnnotationFormatEntity, Integer> {

	@Query("select a from WebAnnotationFormatEntity a where (?1 is null or a.format_id = ?1) and (?2 is null or a.format like %?2%) and (?3 is null or a.bodyEntity = ?3) and (?4 is null or a.targetEntity = ?4)")
	List<WebAnnotationFormatEntity> findByParams(Integer format_id, String format, Integer bodyEntity, Integer target_id);

}