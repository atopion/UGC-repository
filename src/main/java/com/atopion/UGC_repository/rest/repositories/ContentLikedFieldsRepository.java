package com.atopion.UGC_repository.rest.repositories;

import com.atopion.UGC_repository.rest.entities.ContentLikedFieldsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContentLikedFieldsRepository extends JpaRepository<ContentLikedFieldsEntity, Integer> {

	@Query("select a from ContentLikedFieldsEntity a where (?1 is null or a.field_id = ?1) and (?2 is null or a.field_name like %?2%) and (?3 is null or a.field_like_count = ?3) and (?4 is null or a.record_id = ?4)")
	List<ContentLikedFieldsEntity> findByParams(Integer field_id, String field_name, Integer field_like_count, Integer record_id);

}