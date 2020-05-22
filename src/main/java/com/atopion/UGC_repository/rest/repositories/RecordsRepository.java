package com.atopion.UGC_repository.rest.repositories;

import com.atopion.UGC_repository.rest.entities.RecordsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordsRepository extends JpaRepository<RecordsEntity, Integer> {

	@Query("select a from RecordsEntity a where (?1 is null or a.record_id = ?1) and (?2 is null or a.record_identifier like %?2%)")
	List<RecordsEntity> findByParams(Integer record_id, String record_identifier);

}