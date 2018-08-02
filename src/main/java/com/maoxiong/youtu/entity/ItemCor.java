package com.maoxiong.youtu.entity;

public class ItemCor {

	private double x;
	private double y;
	private double width;
	private double height;
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	
	@Override
	public String toString() {
		return "ItemCor [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}
	
}
