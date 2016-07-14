package com.whereIsTime.Services;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whereIsTime.repo.*;
import com.whereIsTime.entities.*;

@Service
public class TaskService {
	@Autowired
	private TaskRepo taskRepo;

	@Autowired
	private TaskItemRepo taskItemRepo;

	@Autowired
	private UserService us;

	@Autowired
	private UserRepo uRepo;
	
	@Autowired
	private ClassificationRepo cRepo;

	/**
	 * 创建任务
	 * <p>
	 * 任务的beginTime 和 endTime 由MTomatoService维护
	 * </p>
	 * 
	 * @param name
	 *            任务名
	 * @param planBeginTime
	 *            计划开始时间
	 * @param planEndTime
	 *            计划结束时间
	 * @param ctid
	 *            所属目录id
	 * @return 保存的任务
	 * @see MTomatoService#completeMTomato(Long,
	 *      com.whereIsTime.entities.User.Status, Date, Date, Integer, List)
	 */
	public Task addTask(String name, List<Long> cids) {
		Task t = new Task();
		for (int i = 0; i < cids.size(); i++) {
			Classification c = cRepo.findOne(cids.get(i));
			if (c == null)
				return null;
			if (i == 0) {
				User u = c.getUser();
				t.setName(name);
				t.setUser(u);
			}
			t.addClassification(c);
		}
		t = taskRepo.save(t);
		return getTaskWithTags(t.getId());
	}

	/**
	 * 更新任务
	 * 
	 * @param ut
	 *            更新后的task
	 * @return 更新后的task/null
	 */
	public Task updateTask(Task ut) {
		if (taskRepo.findOne(ut.getId()) != null) {
			Task tt = taskRepo.save(ut);
			return getTaskWithTags(tt.getId());
		}
		return null;
	}

	/**
	 * 删除任务
	 * 
	 * @param tid
	 *            任务的id
	 */
	public void deleteTask(Long tid) {
		if (taskRepo.findOne(tid) != null) {
			taskRepo.delete(tid);
		}
		
	}

	/**
	 * 创建子项
	 * 
	 * @param tid
	 *            所属任务id
	 * @param name
	 *            子项名
	 */
	public TaskItem addItem(Long tid, String name) {
		Task t = taskRepo.findOne(tid);
		TaskItem item = null;
		if (t != null) {
			item = new TaskItem();
			item.setName(name);
			item.setTask(t);
			item = taskItemRepo.save(item);
		}
		return item;
	}

	/**
	 * 删除子项
	 * 
	 * @param itemId
	 *            子项id
	 */
	public void deleteItem(Long itemId) {
		if (taskItemRepo.findOne(itemId) != null) {
			taskItemRepo.delete(itemId);
		}
	}


	/**
	 * 查询任务(包括外键)
	 * 
	 * @param tid
	 *            任务id
	 * @return tid对应的任务
	 */
	public Task getTask(Long tid) {
		Task t = taskRepo.findOne(tid);
		if (t != null) {
			Task itmp = taskRepo.fetchTaskItems(tid);
			if (itmp != null) {
				t = itmp;
			} else {
				t.setTaskItems(new ArrayList<TaskItem>());
			}
			Task mtmp = taskRepo.fetchMTomatos(tid);
			if (mtmp != null) {
				t.setMtomatos(mtmp.getMtomatos());
			} else {
				t.setMtomatos(new ArrayList<Mtomato>());
			}
			Task ctmp = taskRepo.fetchClassifications(tid);
			if (ctmp != null) {
				t.setClassifications(ctmp.getClassifications());
			} else {
				t.setClassifications(new ArrayList<Classification>());
			}
		}
		return t;
	}
	
	public Task getTaskWithTags(Long tid) {
		Task t = taskRepo.findOne(tid);
		if (t == null)
			return null;
		Task ctmp = taskRepo.fetchClassifications(tid);
		if (ctmp == null) {
			t.setClassifications(new ArrayList<Classification>());
			return t;
		}
		return ctmp;
	}
	
	public Task getTaskOnly(Long tid) {
		Task t = taskRepo.findOne(tid);
		if (t != null) {
			t.setMtomatos(new ArrayList<Mtomato>());
			t.setTaskItems(new ArrayList<TaskItem>());
		}
		return t;
	}

	/**
	 * 完成一个子项
	 * 
	 * @param itemId
	 *            子项id
	 * @param endTime
	 *            完成时间
	 */
	public void completeTaskItem(Long itemId, Date endTime) {
		TaskItem item = taskItemRepo.findOne(itemId);
		
		if (item != null) {
			item.setStatus(TaskItem.Status.COMPLETED);
			taskItemRepo.save(item);
		}
		Task t = item.getTask();
		if (t != null) {
			t = taskRepo.fetchTaskItems(t.getId());
			if (t.allComplete()) {
				t.setStatus(Task.Status.COMPLETED);
				t.setEndTime(endTime);
				taskRepo.save(t);
			}
		}
	}
	/**
	 * 某个task在某天花费的时间，以小时计
	 * @param day
	 * @param tid
	 * @return
	 */
	public Double getTaskTime(String day, Long tid) {
		Task t = taskRepo.fetchMTomatos(tid);
		if (t == null)
			return 0.0;
		Double ret = 0.0;
		List<Mtomato> mtl = t.getMtomatos();
		Mtomato tmp;
		for (int i = 0; i < mtl.size(); i++) {
			tmp = mtl.get(i);
			if (tmp.getDay().equals(day)) {
				ret += baseEntity.calInterval(tmp.getBeginTime(), tmp.getEndTime());
			}
		}
		return ret;
	}
	/**
	 * 某个任务在一段时间内耗费的时间
	 * @param tid
	 * @param from
	 * @param to
	 * @return
	 */
	public Double getTaskInterval(Long tid, Date from, Date to) {
		Task t = taskRepo.fetchMTomatos(tid);
		if (t == null)
			return 0.0;
		Double ret = 0.0;
		List<Mtomato> mtl = t.getMtomatos();
		Mtomato tmp;
		for (int i = 0; i < mtl.size(); i++) {
			tmp = mtl.get(i);
			if (tmp.getBeginTime().getTime() > from.getTime()
				&& tmp.getEndTime().getTime() < to.getTime()) {
				ret += baseEntity.calInterval(tmp.getBeginTime(), tmp.getEndTime());
			}
		}
		return ret;
	}
	/**
	 * 查找完成/未完成的任务
	 * 
	 * @param status
	 *            completed/live
	 * @param uid
	 *            用户id
	 * @return List 结果列表
	 */
	public List<Task> getTasksByStatus(Task.Status status, Long uid) {
		User u = us.getUserOnly(uid);
		List<Task> tmp = null;
		if (u != null) {
			tmp = taskRepo.findByStatusAndUser(status, u);
			List<Task> ret = new ArrayList<Task>();
			for (int i = 0; i < tmp.size(); i++) {
				ret.add(getTaskWithTags(tmp.get(i).getId()));
			}
			return ret;
		}
		return null;
	}

	/**
	 * 用户的所有task
	 * 
	 * @param uid
	 *            用户id
	 * @return task列表
	 */
	public List<Task> getTasksByUser(Long uid) {
		User u = us.getUserOnly(uid);
		if (u != null) {
			List<Task> tmp = taskRepo.findByUser(u);
			List<Task> ret = new ArrayList<Task>();
			for (int i = 0; i < tmp.size(); i++) {
				ret.add(getTaskWithTags(tmp.get(i).getId()));
			}
			return ret;
		}
		return null;
	}
}
