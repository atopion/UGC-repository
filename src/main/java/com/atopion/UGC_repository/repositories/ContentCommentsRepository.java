package com.atopion.UGC_repository.repositories;

import com.atopion.UGC_repository.entities.ContentCommentsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface ContentCommentsRepository extends JpaRepository<ContentCommentsEntity, Integer> {

	@Query("select a from ContentCommentsEntity a where (?1 is null or a.comment_id = ?1) and (?2 is null or a.comment_text like %?2%) and (?3 is null or a.comment_created = ?3) and (?4 is null or a.application_id = ?4) and (?5 is null or a.record_id = ?5) and (?6 is null or a.user_token like %?6%)")
	List<ContentCommentsEntity> findByParams(Integer comment_id, String comment_text, Date comment_created, Integer application_id, Integer record_id, String user_token);

}