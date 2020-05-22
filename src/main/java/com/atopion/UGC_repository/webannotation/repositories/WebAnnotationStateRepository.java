package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationStateEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationStateRepository extends JpaRepository<WebAnnotationStateEntity, Integer> {

	@Query("select a from WebAnnotationStateEntity a where (?1 is null or a.state_id = ?1) and (?2 is null or a.id like %?2%) and (?3 is null or a.type like %?3%) and (?4 is null or a.sourceDateStart = ?4) and (?5 is null or a.sourceDateEnd = ?5) and (?6 is null or a.value like %?6%) and (?7 is null or a.bodyEntity = ?7) and (?8 is null or a.targetEntity = ?8) and (?9 is null or a.refiningState = ?9) and (?10 is null or a.refiningSelector = ?10)")
	List<WebAnnotationStateEntity> findByParams(Integer stateEntity, String id, String type, Date sourceDateStart, Date sourceDateEnd, String value, Integer bodyEntity, Integer targetEntity, Integer refiningState, Integer refiningSelector);

}