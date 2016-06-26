package com.whereIsTime;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.whereIsTime.entities.*;
import com.whereIsTime.repo.*;
import com.whereIsTime.Services.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WhereIsTimeRepoServiceApplication.class)
public class ServiceTest {
	@Autowired
	UserService us;
	@Autowired
	TaskService ts;
	@Autowired
	ClassificationService cls;
	@Autowired
	MTomatoService ms;
	
	@Test
	public void test() {
		//sign up
		User u = us.signUp("john", "123");
		assertTrue(u != null);
		
		//update
		u.setName("John");
		us.updateUser(u);
		assertTrue(u != null && u.getName() == "John");
		
		//getUserOnly
		u = us.getUserOnly(u.getId());
		assertTrue(u.getName().equals("John"));
		
		//getUserByName
		u = us.getUserByName("John");
		assertTrue(u != null);
		
		//list classifications
		List<Classification> cs = cls.getAllByUser(u.getId());
		assertTrue(cs.size() == 5);
		
		List<Long> csids = new ArrayList<Long>();
		for (int i = 0; i < 5; i++) {
			csids.add(cs.get(i).getId());
		}
		Task t1 = ts.addTask("TaskA", csids);
		Task t2 = ts.addTask("TaskB", csids);
		
		TaskItem ti1 = ts.addItem(t1.getId(), "item1");
		TaskItem ti2 = ts.addItem(t2.getId(), "item2");
		
		List<Long> cpItems = new ArrayList<Long>();
		cpItems.add(ti1.getId());
		Mtomato tto = ms.completeMTomato(t1.getId(), Mtomato.feedback.bad, false, true, new Date(1464098245602L), new Date(), 3, cpItems);
		
		ts.deleteTask(t1.getId());
		
		u = us.getUser(u.getId());
		assertTrue(u.getTasks().size() == 1);
		assertTrue(u.getClassifications().size() == 5);
		
		Classification c = cls.getOne(cs.get(0).getId());
		assertTrue(c.getTasks().size() == 1);
		
		Classification c1 = cls.addOne("myTag", u.getId());
		assertTrue(c1.getTasks().size() == 0);
		
		List<Long> one = new ArrayList<Long>();
		one.add(c1.getId());
		
		Task t3 = ts.addTask("TaskC", one);
		Date d = new Date();
		String today = DateFormat.getDateInstance().format(d);
		Double zero = cls.getTagTime(c1.getId(), today);
		assertTrue(zero.equals(0.0));
		
		Long startT = new Date().getTime() - 10000L;
		Mtomato tto2 = ms.completeMTomato(t3.getId(), Mtomato.feedback.great, false, false, new Date(startT), new Date(), 1, new ArrayList<Long>());
		Double notZero = cls.getTagTime(c1.getId(), today);
		assertTrue(notZero > 0.0);
		
		Date from1 = new Date(new Date().getTime() - 100000L);
		Date from2 = new Date(new Date().getTime() + 100000L);
		Date to = new Date();
		zero = cls.getTagInterval(c1.getId(), from2, to);
		assertTrue(zero.equals(0.0));
		
		notZero = cls.getTagInterval(c1.getId(), from1, to);
		assertTrue(notZero > 0.0);
		
		List<Mtomato> tomatos = ms.getByTaskAndDay(t3.getId(), today);
		assertTrue(tomatos.size() == 1);
		
		List<Task> completed = ts.getTasksByStatus(Task.Status.COMPLETED, u.getId());
		assertTrue(completed.size() == 0);
		
		List<Task> all = ts.getTasksByUser(u.getId());
		assertTrue(all.size() == 2);
	}
}
