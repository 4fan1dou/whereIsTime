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
public interface TaskRepo extends JpaRepository<Task, Long> {
	@Query("select distinct t from Task t join fetch t.taskItems " + "where t.id = :tid")
	Task fetchTaskItems(@Param("tid") Long tid);

	@Query("select distinct t from Task t join fetch t.mtomatos " + "where t.id = :tid")
	Task fetchMTomatos(@Param("tid") Long tid);

	Task findByCatalogAndName(Catalog c, String name);

	List<Task> findByCatalog(Catalog c);

	List<Task> findByUser(User u);

	List<Task> findByStatusAndUser(Task.Status status, User u);
}
