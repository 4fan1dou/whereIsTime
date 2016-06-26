package com.whereIsTime.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whereIsTime.entities.*;

@Repository
@Transactional
public interface UserRepo extends JpaRepository<User, Long> {
	User findByName(String uname);

	@Query("select distinct u from User u join fetch u.classifications " + "where u.id = :uid")
	User fetchClassifications(@Param("uid") Long uid);

	@Query("select distinct u from User u join fetch u.tasks " + "where u.id = :uid")
	User fetchTasks(@Param("uid") Long uid);
	
}
