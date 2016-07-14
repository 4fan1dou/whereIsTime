package com.whereIsTime.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TagQIntervalForm {
	Long cid;
	Date from, to;
}
