package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationPurposeEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationPurposeRepository extends JpaRepository<WebAnnotationPurposeEntity, Integer> {

	@Query("select a from WebAnnotationPurposeEntity a where (?1 is null or a.purpose_id = ?1) and (?2 is null or a.purpose like %?2%) and (?3 is null or a.bodyEntity = ?3) and (?4 is null or a.targetEntity = ?4)")
	List<WebAnnotationPurposeEntity> findByParams(Integer purpose_id, String purpose, Integer bodyEntity, Integer targetEntity);

}