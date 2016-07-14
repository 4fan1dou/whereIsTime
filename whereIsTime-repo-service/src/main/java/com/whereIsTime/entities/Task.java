package com.whereIsTime.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "Task")
@Entity
public class Task extends baseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7481555109907855964L;

	public static enum Status {
		LIVE, COMPLETED;
	}

	public Task() {
		super();
	}

	@Column(nullable = false)
	private @Getter @Setter Status status = Status.LIVE;

	@Column(nullable = false)
	private @Getter @Setter String name;

	@Column
	private @Getter @Setter String description = "";

	@Column
	private @Getter @Setter Date beginTime = null;

	@Column
	private @Getter @Setter Date endTime = null;

	@ManyToOne
	private @Getter @Setter User user;

	@OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE)
	private @Getter @Setter List<TaskItem> taskItems = new ArrayList<TaskItem>();

	@OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE)
	private @Getter @Setter List<Mtomato> mtomatos = new ArrayList<Mtomato>();

	@ManyToMany
	private @Getter @Setter List<Classification> classifications = new ArrayList<Classification>();
	
	/**
	 * 使用之前需要确保taskItems已经取到
	 * 
	 * @return 任务的子项是否全部完成
	 * @see com.whereIsTime.Services.TaskService#getTask(java.lang.Long)
	 */
	public boolean allComplete() {
		if (taskItems == null)
			return false;
		for (TaskItem item : taskItems) {
			if (item.getStatus() != TaskItem.Status.COMPLETED) {
				return false;
			}
		}
		return true;
	}

	public void addTaskItem(TaskItem item) {
		taskItems.add(item);
	}

	public void addMtomatos(Mtomato m) {
		mtomatos.add(m);
	}
	
	public void addClassification(Classification c) {
		classifications.add(c);
	}
}
