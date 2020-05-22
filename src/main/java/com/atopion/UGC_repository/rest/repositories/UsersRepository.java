package com.atopion.UGC_repository.rest.repositories;

import com.atopion.UGC_repository.rest.entities.UsersEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {

	@Query("select a from UsersEntity a where (?1 is null or a.user_id = ?1) and (?2 is null or a.user_token like %?2%) and (?3 is null or a.user_name like %?3%) and (?4 is null or a.user_email like %?4%)")
	List<UsersEntity> findByParams(Integer user_id, String user_token, String user_name, String user_email);

}