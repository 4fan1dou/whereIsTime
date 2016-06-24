package com.whereIsTime.Services;

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
	
	public Classification getByName(String cname) {
		return cRepo.findByName(cname);
	}
	
	public void deleteOne(Long cid) {
		cRepo.delete(cid);
	}
	
	public Classification getOne(Long cid) {
		Classification ret = cRepo.findOne(cid);
		if (ret != null) {
			return cRepo.fetchCatalogs(cid);
		}
		return null;
	}
	
	public List<Classification> getAllByUser(Long uid) {
		User u = uRepo.findOne(uid);
		if (u != null) {
			return cRepo.findByUser(u);	
		}
		return null;
	}
	
	public Classification addOne(String name, Long uid) {
		Classification ret = null;
		User u = uRepo.findOne(uid);
		if (u != null) {
			ret = new Classification();
			ret.setName(name);
			ret.setUser(u);
			return cRepo.save(ret);
		}
		return null;
	}
}
