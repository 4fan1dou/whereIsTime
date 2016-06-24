package com.whereIsTime.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whereIsTime.entities.*;

@Repository
@Transactional
public interface ClassificationRepo extends JpaRepository<Classification, Long>{
	List<Classification> findByUser(User u);
	
	@Query(
			"select distinct c from Classification c join fetch c.catalogs "
			+ "where c.id = :cid"
			)
	Classification fetchCatalogs(@Param("cid") Long cid);
	
	
	Classification findByName(String name);
}