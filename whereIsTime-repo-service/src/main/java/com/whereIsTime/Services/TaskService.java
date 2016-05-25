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
	private CatalogRepo ctRepo;

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
	public Task addTask(String name, Date planBeginTime, Date planEndTime, Long ctid) {
		Catalog ct = ctRepo.findOne(ctid);
		if (ct == null)
			return null;
		User u = ct.getUser();
		User utmp = uRepo.fetchTasks(u.getId());
		if (utmp != null) {
			u = utmp;
		} else {
			u.setTasks(new HashSet<Task>());
		}
		Task t = new Task();
		t.setName(name);
		t.setPlanBeginTime(planBeginTime);
		t.setPlanEndTime(planEndTime);
		t.setUser(u);
		t.setCatalog(ct);
		taskRepo.save(t);
		u.addTask(t);
		uRepo.save(u);
		return t;
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
			return taskRepo.save(ut);
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
		taskRepo.delete(tid);
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
			Task ttmp = taskRepo.fetchTaskItems(t.getId());
			if (ttmp != null) {
				t = ttmp;
			} else {
				t.setTaskItems(new HashSet<TaskItem>());
			}
			item.setTask(t);
			taskItemRepo.save(item);
			t.addTaskItem(item);
			taskRepo.save(t);
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
		taskItemRepo.delete(itemId);
	}

	/**
	 * 查询任务(包括外键)
	 * 
	 * @param name
	 *            任务名
	 * @return 任务
	 */
	public Task getTaskByName(String name, Catalog c) {
		Task t = taskRepo.findByCatalogAndName(c, name);
		if (t != null) {
			return getTask(t.getId());
		}
		return null;
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
				t.setTaskItems(itmp.getTaskItems());
			} else {
				t.setTaskItems(new HashSet<TaskItem>());
			}

			Task mtmp = taskRepo.fetchMTomatos(tid);
			if (mtmp != null) {
				t.setMtomatos(mtmp.getMtomatos());
			} else {
				t.setMtomatos(new HashSet<Mtomato>());
			}
		}
		return t;
	}

	/**
	 * 完成一个子项，一般被MtomatoService中的completeMTomato调用
	 * 
	 * @param itemName
	 *            子项名
	 * @param t
	 *            对应的任务
	 * @param endTime
	 *            完成时间
	 * @see com.whereIsTime.Services.MTomatoService#completeMTomato(Long,
	 *      com.whereIsTime.entities.User.Status, Date, Date, Integer, List)
	 */
	public void completeTaskItem(Task t, String itemName, Date endTime) {
		TaskItem item = taskItemRepo.findByTaskAndName(t, itemName);
		if (item != null) {
			item.setStatus(TaskItem.Status.COMPLETED);
			taskItemRepo.save(item);
		}
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
	 * 根据和现在时间的毫秒间隔查找更新过的Task
	 * 
	 * @param interval
	 *            时间间隔,按小时计
	 * @param uid
	 *            用户id
	 * @return List 结果列表
	 */
	public List<Task> getTasksByUpdateDate(Double interval, Long uid) {
		User u = us.getUserOnly(uid);
		List<Task> ret = null;
		if (u != null) {
			List<Task> allTasks = taskRepo.findByUser(u);
			ret = new ArrayList<Task>();
			for (Task task : allTasks) {
				if (baseEntity.calInterval(task.getUpdatedAt(), new Date()) <= interval) {
					ret.add(task);
				}
			}
		}
		return ret;
	}

	/**
	 * 查找完成/未完成的任务
	 * 
	 * @param status
	 *            completed/live
	 * @see com.whereIsTime.entities.Task.Status
	 * @param uid
	 *            用户id
	 * @return List 结果列表
	 */
	public List<Task> getTasksByStatus(Task.Status status, Long uid) {
		User u = us.getUserOnly(uid);
		List<Task> ret = null;
		if (u != null) {
			ret = taskRepo.findByStatusAndUser(status, u);
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
			return taskRepo.findByUser(u);
		}
		return null;
	}

	/**
	 * 目录下的所有task
	 * 
	 * @param c
	 *            目录
	 * @return task列表
	 */
	public List<Task> getTasksByCatalog(Catalog c) {
		return taskRepo.findByCatalog(c);
	}
}
