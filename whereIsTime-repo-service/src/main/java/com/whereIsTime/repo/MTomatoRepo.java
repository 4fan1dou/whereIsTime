package com.whereIsTime.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whereIsTime.entities.*;

@Repository
@Transactional
public interface MTomatoRepo extends JpaRepository<Mtomato, Long> {
	List<Mtomato> findByTask(Task t);
	
	List<Mtomato> findByTaskAndDay(Task t, String day);	
}
