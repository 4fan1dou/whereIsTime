package com.whereIsTime.Services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whereIsTime.repo.*;
import com.whereIsTime.entities.*;


@Service
public class MTomatoService {
	@Autowired
	private MTomatoRepo mTomatoRepo;
	
	@Autowired
	private TaskRepo taskRepo;
	
	@Autowired
	private UserRepo uRepo;
	
	public Mtomato completeMTomato(Long tid, User.Status feedback, Date beginTime, Date endTime, Integer nt) {	
		Task t = taskRepo.findOne(tid);
		Mtomato tomato = null;
		if (t != null) {
			tomato = new Mtomato();
				tomato.setNt(nt);
				tomato.setFeedBack(feedback);
				tomato.setBeginTime(beginTime);
				tomato.setEndTime(endTime);
			tomato.setTask(t);
			mTomatoRepo.save(tomato);
			/*
			 * 更改User的5个描述
			 */
			User u = t.getUser();
			int index = feedback.ordinal();
			u.addStatusWeight(index, baseEntity.calInterval(beginTime, endTime));
			uRepo.save(u);
		}
		return tomato;
	}
}
