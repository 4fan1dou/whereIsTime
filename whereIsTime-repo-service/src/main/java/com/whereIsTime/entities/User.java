package com.whereIsTime.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Table(name = "Users")
@Entity
public class User extends baseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6851471651496778671L;
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER = "ROLE_USER";
	public static enum Status {
		level1, level2, level3, level4, level5;
	}

	// UserStatusWeights
	private Double[] statusWeight = new Double[5];
	public User() {
		super();
		for (int i = 0; i < 5; i++) {
			statusWeight[i] = 0.0;
		}
	}

	public User(String name, String pw) {
		super();
		this.name = name;
		this.pw = pw;
		for (int i = 0; i < 5; i++) {
			statusWeight[i] = 0.0;
		}
	}

	@Column(nullable = false, unique = true)
	private @Setter @Getter String name;

	@Column(nullable = false)
	private @Setter @Getter String pw;

	@Column(nullable = false)
	private @Setter @Getter String role = ROLE_USER;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private @Getter @Setter Set<Task> tasks = new HashSet<Task>();
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
	private @Getter @Setter Set<Classification> classifications = new HashSet<Classification>();

	public void addTask(Task t) {
		tasks.add(t);
	}

	public void addClassification(Classification c) {
		classifications.add(c);
	}
	
	/**
	 * 更新User的描述
	 * 
	 * @param index
	 *            增加权重的坐标
	 * @param n
	 *            增加值
	 * @return Double 该坐标的当前权重
	 * @see com.whereIsTime.Services.MTomatoService#completeMTomato
	 * 
	 */
	public Double addStatusWeight(int index, Double n) {
		statusWeight[index] += n;
		return statusWeight[index];
	}

}
