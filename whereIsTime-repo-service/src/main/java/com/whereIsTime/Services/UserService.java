package com.whereIsTime.Services;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whereIsTime.repo.*;
import com.whereIsTime.entities.*;

@Service
public class UserService {
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ClassificationRepo cRepo;

	/**
	 * 查询用户(将外键列表一并返回)
	 * 
	 * @param uid
	 *            用户的id
	 * @return uid对应的User
	 * @see User
	 */
	public User getUser(Long uid) {
		User u = userRepo.findOne(uid);
		if (u != null) {
			User uctmp = userRepo.fetchClassifications(uid);
			User uttmp = userRepo.fetchTasks(uid);
			if (uctmp != null) {
				u.setClassifications(uctmp.getClassifications());
			} else {
				u.setClassifications(new HashSet<Classification>());
			}
			if (uttmp != null) {
				u.setTasks(uttmp.getTasks());
			} else {
				u.setTasks(new HashSet<Task>());
			}

		}
		return u;
	}
	
	/**
	 * 查询用户(不初始化外键列表)
	 * 
	 * @param uid
	 *            用户的id
	 * @return uid对应的User
	 * @see User
	 */
	public User getUserOnly(Long uid) {
		User u = userRepo.findOne(uid);
		u.setTasks(new HashSet<Task>());
		u.setClassifications(new HashSet<Classification>());
		return u;
	}

	/**
	 * 修改用户
	 * 
	 * @param u
	 *            修改后的用户，注意这里u的id必须不变
	 * @return 修改后的用户
	 */
	public User updateUser(User u) {
		User ret = userRepo.findOne(u.getId());
		if (ret != null) {
			Long uid = u.getId();
			userRepo.save(u);
			return getUserOnly(u.getId());
		}
		return null;
	}

	/**
	 * 查询用户
	 * 
	 * @param name
	 *            用户名
	 * @return name对应的用户
	 */
	public User getUserByName(String name) {
		User u = userRepo.findByName(name);
		if (u != null) {
			Long uid = u.getId();
			return getUserOnly(uid);
		}
		return null;
	}

	/**
	 * 创建用户
	 * 
	 * @param name
	 *            用户名
	 * @param pw
	 *            密码
	 */
	public User signUp(String name, String pw) {
		User duplicate = userRepo.findByName(name);
		if (duplicate != null)
			return null;
		User u = new User(name, pw);
		u = userRepo.save(u);
		String[] tags = new String[5];
		tags[0] = "Important";
		tags[1] = "Urgent";
		tags[2] = "Work";
		tags[3] = "Study";
		tags[4] = "Life";
		for (int i = 0; i < 5; i++) {
			Classification c1 = new Classification();
			c1.setName(tags[i]);
			c1.setUser(u);
			cRepo.save(c1);
		}
		return getUserOnly(u.getId());
	}

	/**
	 * 现有用户数量
	 * 
	 * @return 用户数目
	 */
	public Long userNum() {
		return userRepo.count();
	}
	
	/**
	 * 登录
	 * @param name 用户名
	 * @param pw 密码
	 * @return
	 */
	public User signIn(String name, String pw) {
		User u = userRepo.findByName(name);
		if (u != null && u.getPw().equals(pw)) {
			return getUserOnly(u.getId());
		}
		return null;
	}
	/**
	 * 删除用户，用户创建的其他实体一并删除
	 * 
	 * @param uid
	 *            用户id
	 */
	public void deleteUser(Long uid) {
		if (userRepo.findOne(uid) != null) {
			userRepo.delete(uid);
		}
	}
}
