package com.atopion.UGC_repository.webannotation.repositories;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationSelectorEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface WebAnnotationSelectorRepository extends JpaRepository<WebAnnotationSelectorEntity, Integer> {

	@Query("select a from WebAnnotationSelectorEntity a where (?1 is null or a.selector_id = ?1) and (?2 is null or a.value like %?2%) and (?3 is null or a.conformsTo like %?3%) and (?4 is null or a.exact like %?4%) and (?5 is null or a.prefix like %?5%) and (?6 is null or a.suffix like %?6%) and (?7 is null or a.startPos = ?7) and (?8 is null or a.endPos = ?8) and (?9 is null or a.startSelector = ?9) and (?10 is null or a.endSelector = ?10) and (?11 is null or a.refiningSelector = ?11) and (?12 is null or a.bodyEntity = ?12) and (?13 is null or a.targetEntity = ?13)")
	List<WebAnnotationSelectorEntity> findByParams(Integer selector_id, String value, String conformsTo, String exact, String prefix, String suffix, Integer startPos, Integer endPos, Integer startSelector, Integer endSelector, Integer refiningSelector, Integer bodyEntity, Integer targetEntity);

}