package com.whereIsTime.Services;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whereIsTime.repo.*;
import com.whereIsTime.entities.*;

/*和task相关的basic crud*/

@Service
public class TaskService {
	@Autowired
	private TaskRepo taskRepo;
	
	@Autowired
	private TaskItemRepo taskItemRepo;

	@Autowired
	private UserRepo uRepo;
	
	@Autowired
	private CatalogRepo ctRepo;
	
	public Task addTask(String name, 
						Date beginTime, Date endTime, Date planBeginTime, Date planEndTime,
						Long uid, Long cataid) {
		Task t = null;
		User u = uRepo.findOne(uid);
		Catalog ct = ctRepo.findOne(cataid);
		if (u != null && ct != null) {
			t = new Task();
				t.setName(name);
				t.setBeginTime(beginTime);
				t.setEndTime(endTime);
				t.setPlanBeginTime(planBeginTime);
				t.setPlanEndTime(planEndTime);
			t.setUser(u);
			t.setCatalog(ct);
			taskRepo.save(t);
		}
		return t;
	}
	
	public Task updateTask(Task ut) {
		if (taskRepo.findOne(ut.getId()) != null) {
			return taskRepo.save(ut);
		}
		return null;
	}
	
	public void deleteTask(Long tid) {
		taskRepo.delete(tid);
	}
	
	public TaskItem addItem(Long tid, String name) {
		Task t = taskRepo.findOne(tid);
		TaskItem item = null;
		if (t != null) {
			item = new TaskItem();
				item.setName(name);
			
			item.setTask(t);
			taskItemRepo.save(item);
		}
		return item;
	}
	
	public void deleteItem(Long itemId) {
		taskItemRepo.delete(itemId);
	}
	
	
	/*
	 * 遍历t.mTomatos, 计算timeCost和
	 */
	public Double totalTimeCost(Long tid) {
		Task t = taskRepo.findOne(tid);
		Double ret = 0.0;
		if (t != null) {
			t = taskRepo.fetchMTomatos(tid);
			for (Mtomato mt:t.getMtomatos()) {
				ret += baseEntity.calInterval(mt.getBeginTime(), mt.getEndTime());
			}
		}
		return ret;
	}
	
	/*
	 * if t.status == complete, return {complete time} - {plan end time}
	 * else return {current time} - {plan end time}
	 */
	public Double totalDelay(Long tid) {
		Double ret = 0.0;
		Task t = taskRepo.findOne(tid);
		if ( t != null ) {
			Date delayTime = t.getStatus() == Task.Status.COMPLETED ? t.getEndTime() : new Date();
			ret = baseEntity.calInterval(t.getPlanEndTime(), delayTime);
		}
		return ret;
	}
	
	/*
	 * totalTimeCost - planCost
	 */
	public Double totalTimeout(Long tid) {
		Double ret = 0.0;
		Task t = taskRepo.findOne(tid);
		if ( t != null ) {
			Double planTime = baseEntity.calInterval(t.getBeginTime(), t.getEndTime());
			ret = planTime - totalTimeCost(tid);
		}
		return ret;
	}
	
	public Task getTaskByName(String name) {
		return taskRepo.findByName(name);
	}
	
	public Task getTask(Long tid) {
		Task t = taskRepo.findOne(tid);
		if (t != null) {
			t = taskRepo.fetchTaskItems(tid);
			t.setMtomatos(taskRepo.fetchMTomatos(tid).getMtomatos());
		}
		return t;
	}
	
	public Task completeTaskItem(String itemName) {
		TaskItem item = taskItemRepo.findByName(itemName);
		if (item != null) {
			item.setStatus(TaskItem.Status.COMPLETED);
			taskItemRepo.save(item);
		}
		Task t = item.getTask();
		if (t != null ) {
			t = taskRepo.fetchTaskItems(t.getId());
			if (t.allComplete()) {
				t.setStatus(Task.Status.COMPLETED);
			}
		}
		return t;
	}
	
	/*根据和现在时间的毫秒间隔查找更新过的Task*/
	public List<Task> getTasksByUpdateDate(Double interval, Long uid) {
		User u = uRepo.findOne(uid);
		List<Task> ret = null;
		if (u != null) {
			List<Task> allTasks = taskRepo.findByUser(u);
			ret = new ArrayList<Task>();
			for (Task task:allTasks) {
				if (baseEntity.calInterval(task.getUpdatedAt(), new Date()) <= interval) {
					ret.add(task);
				}
			}
		}
		return ret;
	}
	/*查找完成/未完成的任务*/
	public List<Task> getTasksByStatus(Task.Status status, Long uid) {
		User u = uRepo.findOne(uid);
		List<Task> ret = null;
		if (u != null) {
			ret = taskRepo.findByStatusAndUser(status, u);
		}
		return ret;
	}
}
