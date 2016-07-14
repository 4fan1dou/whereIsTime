package com.whereIsTime.dto;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter

public class TomatoForm {
	Long tid;
	Date beginTime, endTime;
	List<Long> completedItems;
	Integer nt;
}
