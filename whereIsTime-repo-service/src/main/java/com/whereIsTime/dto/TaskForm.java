package com.whereIsTime.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TaskForm {
	String  name;
	List<Long> cids = new ArrayList<Long>();
}
