package com.atopion.UGC_repository.repositories;

import com.atopion.UGC_repository.entities.ContentAnnotationsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface ContentAnnotationsRepository extends JpaRepository<ContentAnnotationsEntity, Integer> {

	@Query("select a from ContentAnnotationsEntity a where (?1 is null or a.annotation_id = ?1) and (?2 is null or a.annotation_url like %?2%) and (?3 is null or a.annotation_content like %?3%) and (?4 is null or a.annotation_canvas = ?4) and (?5 is null or a.annotation_created = ?5) and (?6 is null or a.application_id = ?6) and (?7 is null or a.record_id = ?7) and (?8 is null or a.user_token like %?8%)")
	List<ContentAnnotationsEntity> findByParams(Integer annotation_id, String annotation_url, String annotation_content, Integer annotation_canvas, Date annotation_created, Integer application_id, Integer record_id, String user_token);

}