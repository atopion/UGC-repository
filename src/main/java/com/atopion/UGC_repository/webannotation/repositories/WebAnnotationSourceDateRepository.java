package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationSourceDateEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationSourceDateRepository extends JpaRepository<WebAnnotationSourceDateEntity, Integer> {

	@Query("select a from WebAnnotationSourceDateEntity a where (?1 is null or a.source_date_id = ?1) and (?2 is null or a.source_date = ?2) and (?3 is null or a.stateEntity = ?3)")
	List<WebAnnotationSourceDateEntity> findByParams(Integer source_date_id, Date source_date, Integer stateEntity);

}