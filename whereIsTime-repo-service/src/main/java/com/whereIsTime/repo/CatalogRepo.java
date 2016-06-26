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
public interface CatalogRepo extends JpaRepository<Catalog, Long>{
	@Query(
			"select distinct c from Catalog c join fetch c.tasks "
			+ "where c.id = :cid"
			)
	Catalog fetchTasks(@Param("cid") Long cid);
	
	List<Catalog> findByUser(User u);
	
	List<Catalog> findByClassification(Classification c);
}
