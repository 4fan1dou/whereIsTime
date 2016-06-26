package com.whereIsTime.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whereIsTime.entities.*;
import com.whereIsTime.repo.*;

@Service
public class CatalogService {
	@Autowired
	CatalogRepo cataRepo;

	@Autowired
	UserRepo uRepo;

	@Autowired
	ClassificationRepo cRepo;
	
	public Catalog addOne(String name, Long uid, Long cid) {
		Classification c = cRepo.findOne(cid);
		User u = uRepo.findOne(uid);
		if (u != null && c != null) {
			Catalog ret = new Catalog();
				ret.setName(name);
			ret.setClassification(c);
			ret.setUser(u);
			cataRepo.save(ret);
			return ret;
		}
		return null;
	}
	
	public void deleteOne(Long cataid) {
		cataRepo.delete(cataid);
	}
	
	public List<Catalog> getByUser(User u) {
		return cataRepo.findByUser(u);
	}
	
	public List<Catalog> getByClassification(Classification c) {
		return cataRepo.findByClassification(c);
	}
}
