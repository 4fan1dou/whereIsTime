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

@Table(name = "Classification")
@Entity
public class Classification extends baseEntity {
	
	/**
	 * 
	 */
	public Classification () {super();}
	private static final long serialVersionUID = 4997129228943399412L;
	@Column(nullable = false)
	private @Getter @Setter String name;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private @Getter @Setter User user;
	
	
	@OneToMany(mappedBy = "classification")
	private @Getter @Setter Set<Catalog> catalogs = new HashSet<Catalog>();
	
}
