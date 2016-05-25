package com.whereIsTime.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "Catalog")
@Entity
public class Catalog extends baseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1004540756695678203L;

	public Catalog() {
		super();
	}

	@Column
	private @Getter @Setter String name;

	@ManyToOne
	private @Getter @Setter User user;

	@ManyToOne
	private @Getter @Setter Classification classification;

	@OneToMany(mappedBy = "catalog", cascade = CascadeType.REMOVE)
	private @Getter @Setter Set<Task> tasks = new HashSet<Task>();

	public void addTask(Task t) {
		tasks.add(t);
	}

}
