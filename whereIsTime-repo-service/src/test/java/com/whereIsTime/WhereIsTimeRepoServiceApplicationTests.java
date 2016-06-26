package com.whereIsTime;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.whereIsTime.entities.*;
import com.whereIsTime.repo.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WhereIsTimeRepoServiceApplication.class)
public class WhereIsTimeRepoServiceApplicationTests {
	
	@Autowired
	TaskRepo taskRp;
	
	@Autowired
	TaskItemRepo itemRp;

	@Test
	public void contextLoads() {
	}
	/*
	 * 测试从表在主表中的外键是否为lazy-fetch
	 * 测试结果:可用,不是lazy-fetch
	 * Note: 记得撤销Task中的nullable注释
	 */
	@Test
	public void fkQuery() {
		/*
		 * 新建
		 */
		User u = new User("zhangsan", "123");
		
		Catalog c = new Catalog();
			c.setName("Catalog1");
			c.setUser(u);
		
		Task t = new Task();
			t.setName("Task1");
			t.setBeginTime(new Date());
			t.setEndTime(new Date());
			t.setPlanBeginTime(new Date());
			t.setPlanEndTime(new Date());
			t.setUser(u);
		
		t.setCatalog(c);
		
		TaskItem item = new TaskItem();
			item.setName("TaskItem1");
		
		/*
		 * 外键关联
		 */
		item.setTask(t);
		//t.addTaskItem(item);
		
		/*
		 * 级联保存
		 */
		itemRp.save(item);
		
		/*
		 * 查找item
		 */
		item = itemRp.findByName(item.getName());
		/*
		 * 测试
		 */
		assertTrue(item != null);
		assertTrue(item.getTask() != null);
		assertTrue(item.getTask().getName().equals("Task1"));
		
		/*
		 * 查找task
		 */
		t = taskRp.findByName(t.getName());
		t = taskRp.fetchTaskItems(t.getId());
		/*
		 * 测试
		 */
		assertTrue(t.getTaskItems().size() == 1);
		
	}

}
