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
			User ucatmp = userRepo.fetchCatalogs(uid);
			if (uctmp != null) {
				u.setClassifications(uctmp.getClassifications());
			} else {
				u.setClassifications(new HashSet<Classification>());
			}
			if (ucatmp != null) {
				u.setCatalogs(ucatmp.getCatalogs());
			} else {
				u.setCatalogs(new HashSet<Catalog>());
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
		return userRepo.findOne(uid);
	}

	/**
	 * 修改用户
	 * 
	 * @param u
	 *            修改后的用户，注意这里u的id必须不变
	 * @return 修改后的用户
	 */
	public User updateUser(User u) {
		return userRepo.save(u);
	}

	/**
	 * 查询用户
	 * 
	 * @param name
	 *            用户名
	 * @return name对应的用户
	 */
	public User getUserByName(String name) {
		return userRepo.findByName(name);
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
		return userRepo.save(new User(name, pw));
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
	 * 删除用户，用户创建的其他实体一并删除
	 * 
	 * @param uid
	 *            用户id
	 */
	public void deleteUser(Long uid) {
		userRepo.delete(uid);
	}

	/*
	 * 查看在所有用户中的排名 按照什么排序？
	 */
	// Integer getRank(String uname)
}
