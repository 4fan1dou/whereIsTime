package com.whereIsTime.Services;

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

	/**
	 * 通过name和用户获取分类
	 * 
	 * @param u
	 *            用户
	 * @param cname
	 *            类名
	 * @return 分类
	 */
	public Classification getByUserAndName(User u, String cname) {
		Classification c = cRepo.findByUserAndName(u, cname);
		if (c != null)
			return getOne(c.getId());
		return null;
	}

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
			Classification retmp = cRepo.fetchCatalogs(cid);
			if (retmp != null) {
				ret.setCatalogs(retmp.getCatalogs());
			} else {
				ret.setCatalogs(new HashSet<Catalog>());
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
		cRepo.save(ret);
		u.addClassification(ret);
		uRepo.save(u);
		return ret;
	}
}
