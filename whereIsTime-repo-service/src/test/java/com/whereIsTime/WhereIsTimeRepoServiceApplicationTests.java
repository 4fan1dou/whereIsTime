package com.whereIsTime;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.junit.Test;

import com.whereIsTime.entities.*;
import com.whereIsTime.repo.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WhereIsTimeRepoServiceApplication.class)
public class WhereIsTimeRepoServiceApplicationTests {
	
	@Test
	public void contextLoad() {}

}
