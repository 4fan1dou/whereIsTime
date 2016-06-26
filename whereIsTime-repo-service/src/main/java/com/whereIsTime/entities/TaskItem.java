package com.whereIsTime.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.CascadeType;

import lombok.Getter;
import lombok.Setter;

@Table(name = "TaskItem")
@Entity
public class TaskItem extends baseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2382689625871327514L;

	public TaskItem() {
		super();
	}

	public static enum Status {
		LIVE, COMPLETED;
	}

	@ManyToOne
	private @Getter @Setter Task task;

	@Column(nullable = false)
	private @Getter @Setter Status status = Status.LIVE;

	@Column(nullable = false)
	private @Getter @Setter String name;
}
