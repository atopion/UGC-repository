package com.atopion.UGC_repository.repositories;

import com.atopion.UGC_repository.entities.ApplicationsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Date;

public interface ApplicationsRepository extends JpaRepository<ApplicationsEntity, Integer> {

	@Query("select a from ApplicationsEntity a where (?1 is null or a.application_id = ?1) and (?2 is null or a.application_name like %?2%)")
	List<ApplicationsEntity> findByParams(Integer application_id, String application_name);

}