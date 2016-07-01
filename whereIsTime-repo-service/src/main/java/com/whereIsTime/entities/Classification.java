package com.whereIsTime.entities;

import java.util.ArrayList;
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

@Table(name = "Classification")
@Entity
public class Classification extends baseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4997129228943399412L;
	
	public Classification() {
		super();
	}


	@ManyToMany(mappedBy = "classifications")
	private @Getter @Setter List<Task> tasks = new ArrayList<Task>();
	
	@Column(nullable = false)
	private @Getter @Setter String name;

	@ManyToOne
	private @Getter @Setter User user;
	
	public void addTask(Task t) {
		tasks.add(t);
	}
}
