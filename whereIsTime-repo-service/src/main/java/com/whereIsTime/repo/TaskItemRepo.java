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
public interface TaskItemRepo extends JpaRepository<TaskItem, Long> {
	TaskItem findByTaskAndName(Task t, String name);

	@Query("select item from TaskItem item inner join item.task t " + "where t.name = :tname")
	List<TaskItem> findByTaskName(@Param("tname") String tname);
}
