package com.whereIsTime.entities;

import lombok.Data;
import lombok.Getter;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/*
 * 基础实体
 * 记录更新和创建时间
 * id由数据库维护
 */
@MappedSuperclass
@Data
public abstract class baseEntity implements Comparable<baseEntity>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private @Getter Long id;

	@Column(nullable = false)
	private @Getter Date createdAt;

	@Column(nullable = false)
	private @Getter Date updatedAt;

	@PrePersist
	public void prePersist() {
		createdAt = updatedAt = new Date();
	}

	/*
	 * save时自动更新updateAt
	 */
	@PreUpdate
	public void preUpdate() {
		updatedAt = new Date();
	}

	/**
	 * 给出一个时间段，计算相隔的小时数
	 * 
	 * @param from
	 *            起始时间
	 * @param to
	 *            结束时间
	 * @return double 相隔时间，用小时描述
	 */
	static public Double calInterval(Date from, Date to) {
		Long diff = to.getTime() - from.getTime();
		if (diff < 0) {
			return 0.0;
		}
		Double diffh = ((double) diff) / (60 * 60 * 1000);
		return diffh;
	}

	/**
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(baseEntity o) {
		return this.getId().compareTo(o.getId());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (other == null || other.getClass() != this.getClass())
			return false;

		return this.getId().equals(((baseEntity) other).getId());
	}
}