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
	List<TaskItem> findByTask(Task t);
}
