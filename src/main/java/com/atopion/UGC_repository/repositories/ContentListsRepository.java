package com.atopion.UGC_repository.repositories;

import com.atopion.UGC_repository.entities.ContentListsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface ContentListsRepository extends JpaRepository<ContentListsEntity, Integer> {

	@Query("select a from ContentListsEntity a where (?1 is null or a.list_id = ?1) and (?2 is null or a.list_title like %?2%) and (?3 is null or a.list_description like %?3%) and (?4 is null or a.list_created = ?4) and (?5 is null or a.application_id = ?5) and (?6 is null or a.user_token like %?6%)")
	List<ContentListsEntity> findByParams(Integer list_id, String list_title, String list_description, Date list_created, Integer application_id, String user_token);

}