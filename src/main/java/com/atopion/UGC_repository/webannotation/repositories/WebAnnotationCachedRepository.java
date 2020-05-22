package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationCachedEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationCachedRepository extends JpaRepository<WebAnnotationCachedEntity, Integer> {

	@Query("select a from WebAnnotationCachedEntity a where (?1 is null or a.cached_id = ?1) and (?2 is null or a.cached like %?2%) and (?3 is null or a.stateEntity = ?3)")
	List<WebAnnotationCachedEntity> findByParams(Integer cached_id, String cached, Integer stateEntity);

}