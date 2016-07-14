package com.whereIsTime.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TaskUpdateForm {
	Long id;
	String name;
	List<String> tags = new ArrayList<String>();
}
