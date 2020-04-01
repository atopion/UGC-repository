package com.atopion.UGC_repository.repositories;

import com.atopion.UGC_repository.entities.RecordsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface RecordsRepository extends JpaRepository<RecordsEntity, Integer> {

	@Query("select a from RecordsEntity a where (?1 is null or a.record_id = ?1) and (?2 is null or a.record_identifier like %?2%)")
	List<RecordsEntity> findByParams(Integer record_id, String record_identifier);

}