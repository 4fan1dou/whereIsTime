package com.whereIsTime.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.whereIsTime.entities.*;

@Repository
@Transactional
public interface MTomatoRepo extends JpaRepository<Mtomato, Long>{

}
