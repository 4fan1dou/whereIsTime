package com.chartview.model;

public class Point {

	/**
	 * 值,建议取0-100
	 */
	private float value;

	/**
	 * 颜色
	 */
	private int color;

	/**
	 * 标记
	 */
	private String marker;

	/**
	 * 占整体的百分比（针对饼图）
	 */
	private String percentage;

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getMarker() {
		return marker;
	}

	public void setMarker(String marker) {
		this.marker = marker;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}

	public Point(float value, int color, String marker) {
		this.value = value;
		this.color = color;
		this.marker = marker;
	}

}
