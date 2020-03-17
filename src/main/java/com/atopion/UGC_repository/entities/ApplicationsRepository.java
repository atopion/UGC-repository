package com.atopion.UGC_repository.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationsRepository extends JpaRepository<ApplicationsEntity, Integer> {

    @Query("select a from ApplicationsEntity a where a.application_name = ?1")
    public List<ApplicationsEntity> findByName(String name);
}
