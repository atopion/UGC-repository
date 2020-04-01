package com.atopion.UGC_repository.repositories;

import com.atopion.UGC_repository.entities.ContentLikesEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface ContentLikesRepository extends JpaRepository<ContentLikesEntity, Integer> {

	@Query("select a from ContentLikesEntity a where (?1 is null or a.record_id = ?1) and (?2 is null or a.cuby_like_level_1 = ?2) and (?3 is null or a.cuby_like_level_2 = ?3) and (?4 is null or a.cuby_like_level_3 = ?4)")
	List<ContentLikesEntity> findByParams(Integer record_id, Integer cuby_like_level_1, Integer cuby_like_level_2, Integer cuby_like_level_3);

}