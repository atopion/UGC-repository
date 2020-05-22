package com.atopion.UGC_repository.rest.repositories;

import com.atopion.UGC_repository.rest.entities.ContentListsRecordsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface ContentListsRecordsRepository extends JpaRepository<ContentListsRecordsEntity, Integer> {

	@Query("select a from ContentListsRecordsEntity a where (?1 is null or a.list_content_id = ?1) and (?2 is null or a.entry_created = ?2) and (?3 is null or a.list_id = ?3) and (?4 is null or a.record_id = ?4)")
	List<ContentListsRecordsEntity> findByParams(Integer list_content_id, Date entry_created, Integer list_id, Integer record_id);

}