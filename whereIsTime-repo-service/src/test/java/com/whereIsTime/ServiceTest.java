package com.whereIsTime;

import static org.junit.Assert.*;

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
	CatalogService cs;
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
		
		//add classifications
		cls.addOne("IMPORTANT", u.getId());
		cls.addOne("URGENT", u.getId());
		Classification c1 = cls.getByUserAndName(u, "URGENT");
		u = us.getUser(u.getId());
		assertTrue(u.getClassifications().size() == 2);
		
		// add catalogs
		cs.addOne("Catalog1", c1.getId());
		cs.addOne("Catalog2", c1.getId());
		Catalog cata1 = cs.getByClassificationAndName(c1, "Catalog1");
		c1 = cls.getOne(c1.getId());
		assertTrue(c1.getCatalogs().size() == 2);
		
		//add tasks
		ts.addTask("TaskA", new Date(), new Date(), cata1.getId());
		ts.addTask("TaskB", new Date(), new Date(), cata1.getId());
		Task t = ts.getTaskByName("TaskA", cata1);
		
		// add items
		ts.addItem(t.getId(), "item1");
		ts.addItem(t.getId(), "item2");
		
		//add tomatoes
		List<String> cpItems = new ArrayList<String>();
		cpItems.add("item1");
		cpItems.add("item2");
		Mtomato tto = ms.completeMTomato(t.getId(), Mtomato.feedback.bad, false, true, new Date(1464098245602L), new Date(), 3, cpItems);
		//get tomatoes by task
		t = ts.getTaskByName("TaskA", cata1);
		List<Mtomato> tomatoList = ms.getByTask(t);
		assertTrue(tomatoList.size() == 1);
		
		//get classifications by user
		List<Classification> allcl = cls.getAllByUser(u.getId());
		assertTrue(allcl.size() == 2);
		
		//get catalog by user
		List<Catalog> acl = cs.getByUser(u.getId());
		assertTrue(acl.size() == 2);
		
		//get catalog by classification
		List<Catalog> ccl = cs.getByClassification(c1.getId());
		assertTrue(ccl.size() == 2);
		
		//get task by id
		Task t2 = ts.getTaskByName("TaskB", cata1);
		Task t3 = ts.getTask(t2.getId());
		assertTrue(t3.getName().equals("TaskB"));
		Task t4 = ts.getTask(t.getId());
		assertTrue(t4.getMtomatos().size() == 1 && t4.getTaskItems().size() == 2);
		
		//get tasks by status
		List<Task> tlist = ts.getTasksByStatus(Task.Status.COMPLETED, u.getId());
		assertTrue(tlist.size() == 1);
		List<Task> eptlist = ts.getTasksByStatus(Task.Status.LIVE, u.getId());
		assertTrue(eptlist.size() == 1);
		Task t5 = eptlist.get(0);
		t5.setStatus(Task.Status.COMPLETED);
		ts.updateTask(t5);
		eptlist = ts.getTasksByStatus(Task.Status.LIVE, u.getId());
		assertTrue(eptlist.size() == 0);
		
		//get tasks by interval
		List<Task> tl = ts.getTasksByUpdateDate(0.000001, u.getId());
		assertTrue(tl.size() == 0);
		
		tl = ts.getTasksByUpdateDate(1.0, u.getId());
		assertTrue(tl.size() == 2);
		
		//test cascade remove
		us.deleteUser(u.getId());
	}
}
