package com.whereIsTime.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whereIsTime.repo.*;
import com.whereIsTime.entities.*;

@Service
public class UserService {
	@Autowired
	private UserRepo userRepo;
	
	public User getUser(Long uid) {
		User u = userRepo.findOne(uid);
		if (u != null) {
			u = userRepo.fetchCatalogs(uid);
			u.setClassifications(userRepo.fetchClassifications(uid).getClassifications());
			u.setTasks(userRepo.fetchTasks(uid).getTasks());
			u.setCatalogs(userRepo.fetchCatalogs(uid).getCatalogs());
		}
		return u;
	}
	
	public User updateUser(User u) {
		return userRepo.save(u);
	}
	
	public User getUserByName(String name) {
		return userRepo.findByName(name);
	}
	
	public User signUp(String name, String pw) {
		return userRepo.save(new User(name, pw));
	}
	
	/*
	 * 查看在所有用户中的排名
	 * 按照什么排序？
	 */
	//Integer getRank(String uname)
}
