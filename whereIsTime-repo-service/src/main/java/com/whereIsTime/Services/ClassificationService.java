package com.whereIsTime.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whereIsTime.entities.*;
import com.whereIsTime.repo.*;

@Service
public class ClassificationService {
	@Autowired
	private ClassificationRepo cRepo;
	@Autowired
	private UserRepo uRepo;
	@Autowired
	private TaskService ts;
	/**
	 * 删除分类
	 * 
	 * @param cid
	 *            分类id
	 */
	public void deleteOne(Long cid) {
		cRepo.delete(cid);
	}

	/**
	 * 通过id获取分类(包括外键)
	 * 
	 * @param cid
	 *            分类id
	 * @return cid对应的分类
	 */
	public Classification getOne(Long cid) {
		Classification ret = cRepo.findOne(cid);
		if (ret != null) {
			Classification retmp = cRepo.fetchTasks(cid);
			if (retmp != null) {
				ret = retmp;
			} else {
				ret.setTasks(new ArrayList<Task>());
			}
			return ret;
		}
		return null;
	}

	/**
	 * 查询分类
	 * 
	 * @param uid
	 *            用户id
	 * @return List 用户对应的所有分类
	 */
	public List<Classification> getAllByUser(Long uid) {
		User u = uRepo.findOne(uid);
		if (u != null) {
			return cRepo.findByUser(u);
		}
		return null;
	}

	/**
	 * 创建分类
	 * 
	 * @param name
	 *            String 分类名称
	 * @param uid
	 *            Long 用户id
	 * @return 保存后的分类
	 */
	public Classification addOne(String name, Long uid) {
		Classification ret = new Classification();
		User u = uRepo.findOne(uid);
		if (u == null)
			return null;
		User tmp = uRepo.fetchClassifications(uid);
		if (tmp == null) {
			u.setClassifications(new HashSet<Classification>());
		} else {
			u = tmp;
		}
		ret.setName(name);
		ret.setUser(u);
		ret = cRepo.save(ret);
		ret.setTasks(new ArrayList<Task>());
		return ret;
	}
	
	
	public Double getTagTime(Long cid, String day) {
		Classification ctmp = cRepo.fetchTasks(cid);
		if (ctmp == null)
			return 0.0;
		Double ret = 0.0;
		Task t;
		List<Task> tasks = ctmp.getTasks();
		for (int i = 0; i < tasks.size(); i++) {
			t = tasks.get(i);
			ret += ts.getTaskTime(day, t.getId());
		}
		return ret;
	}
	
	public Double getTagInterval(Long cid, Date from, Date to) {
		Classification ctmp = cRepo.fetchTasks(cid);
		if (ctmp == null)
			return 0.0;
		Double ret = 0.0;
		Task t;
		List<Task> tasks = ctmp.getTasks();
		for (int i = 0; i < tasks.size(); i++) {
			t = tasks.get(i);
			ret += ts.getTaskInterval(t.getId(), from, to);
		}
		return ret;
	}
}
