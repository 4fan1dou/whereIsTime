package com.whereIsTime.entities;

import java.util.Date;
import java.text.DateFormat;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "Mtomato")
@Entity
public class Mtomato extends baseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8474007738347800829L;
	
	public static enum feedback {great, normal, bad};

	public Mtomato() {
		super();
	}

	@Column(nullable = false)
	private @Getter @Setter Integer nt;

	public void setBeginTime(Date t) {
		this.day = DateFormat.getDateInstance().format(t);
		this.beginTime = t;
	}
	
	@Column(nullable = false)
	private @Getter Date beginTime;

	@Column(nullable = false)
	private @Getter String day;

	@Column(nullable = false)
	private @Getter @Setter Date endTime;

	@Column(nullable = false)
	private @Getter @Setter feedback feedBack = feedback.normal;

	@ManyToOne
	private @Getter @Setter Task task;
	
	@Column
	private @Getter @Setter boolean isDelayed = false;
	
	@Column
	private @Getter @Setter boolean isBreaked = false;
}
