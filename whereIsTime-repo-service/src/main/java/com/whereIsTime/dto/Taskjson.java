package com.whereIsTime.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.whereIsTime.entities.Task;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class Taskjson {
	Integer status;
	String name;
	String description;
	Date beginTime;
	Date endTime;
	Long user;
	Long id;
	Date createAt, updateAt;
	List<Long> tags = new ArrayList<Long>();
	public Taskjson(Task t) {
		Task.Status ts = t.getStatus();
		if (ts == Task.Status.COMPLETED) {
			this.status = 0;
		} else {
			this.status = 1;
		}
		this.name = t.getName();
		this.description = t.getDescription();
		this.beginTime = t.getBeginTime();
		this.endTime = t.getEndTime();
		this.user = t.getUser().getId();
		this.id = t.getId();
		this.createAt = t.getCreatedAt();
		this.updateAt = t.getUpdatedAt();
		for (int i = 0; i < t.getClassifications().size(); i++) {
			tags.add(t.getClassifications().get(i).getId());
		}
	}
}
