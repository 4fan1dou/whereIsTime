package com.whereIsTime.Services;

import java.util.HashSet;
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

	/**
	 * 创建目录
	 * 
	 * @param name
	 *            目录名称
	 * @param uid
	 *            用户id
	 * @param cid
	 *            分类id
	 * @return 新创建的目录(带有id)
	 */
	public Catalog addOne(String name, Long cid) {
		Classification c = cRepo.findOne(cid);
		if (c == null)
			return null;
		User u = c.getUser();
		User utmp = uRepo.fetchCatalogs(u.getId());
		if (utmp == null) {
			u.setCatalogs(new HashSet<Catalog>());
		} else {
			u = utmp;
		}
		Catalog ret = new Catalog();
		ret.setName(name);
		ret.setClassification(c);
		ret.setUser(u);
		cataRepo.save(ret);
		u.addCatalog(ret);
		uRepo.save(u);
		return ret;
	}

	/**
	 * 删除目录
	 * <p>
	 * 级联删除目录下的task
	 * </p>
	 * 
	 * @param cataid
	 *            目录id
	 */
	public void deleteOne(Long cataid) {
		cataRepo.delete(cataid);
	}

	/**
	 * 用户对应的所有目录
	 * 
	 * @param u
	 *            用户
	 * @return List 所有目录
	 */
	public List<Catalog> getByUser(Long uid) {
		User u = uRepo.findOne(uid);
		if (u != null)
			return cataRepo.findByUser(u);
		return null;
	}

	/**
	 * 用户分类下的所有目录
	 * 
	 * @param c
	 *            分类 u 用户
	 * @return List 所有目录
	 */
	public List<Catalog> getByClassification(Long cid) {
		Classification cl = cRepo.findOne(cid);
		if (cl != null) {
			return cataRepo.findByClassification(cl);
		}
		return null;
	}

	public Catalog getByClassificationAndName(Classification c, String name) {
		return cataRepo.findByClassificationAndName(c, name);
	}
}
