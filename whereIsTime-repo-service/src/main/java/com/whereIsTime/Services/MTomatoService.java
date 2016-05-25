package com.whereIsTime.Services;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

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

	@Autowired
	private TaskService taskService;

	/**
	 * 在存储一组番茄钟时通过用户的评价改变雷达图的权重
	 * 
	 * @param tid
	 *            本组番茄钟对应任务的id
	 * @param feedback
	 *            用户的自我评价
	 * @param beginTime
	 *            本组番茄钟的开始时间
	 * @param endTime
	 *            本组番茄钟的结束时间
	 * @param nt
	 *            本组番茄钟包括的番茄数
	 * @param completedItems
	 *            本组番茄钟完成的子项列表
	 * @see com.whereIsTime.entities.User#addStatusWeight
	 * @see com.whereIsTime.entities.Mtomato
	 */
	public Mtomato completeMTomato(Long tid, User.Status feedback, Date beginTime, Date endTime, Integer nt,
			List<String> completedItems) {
		Task t = taskRepo.findOne(tid);
		User u = t.getUser();
		Mtomato tomato = null;
		if (t != null) {
			tomato = new Mtomato();
			tomato.setNt(nt);
			tomato.setFeedBack(feedback);
			tomato.setBeginTime(beginTime);
			tomato.setEndTime(endTime);
			Task ttmp = taskRepo.fetchMTomatos(t.getId());
			if (ttmp != null) {
				t = ttmp;
			} else {
				t.setMtomatos(new HashSet<Mtomato>());
			}
			tomato.setTask(t);
			mTomatoRepo.save(tomato);
			t.addMtomatos(tomato);
			taskRepo.save(t);
			/*
			 * 更改User的5个描述
			 */
			int index = feedback.ordinal();
			u.addStatusWeight(index, baseEntity.calInterval(beginTime, endTime));
			uRepo.save(u);

			/*
			 * 更改任务和子项的状态
			 */
			if (t.getBeginTime() == null) {
				t.setBeginTime(beginTime);
				taskRepo.save(t);
			}
			for (String item : completedItems) {
				taskService.completeTaskItem(t, item, endTime);
			}
		}
		return tomato;
	}

	public List<Mtomato> getByTask(Task t) {
		return mTomatoRepo.findByTask(t);
	}
}
