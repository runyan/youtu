package com.maoxiong.youtu.enums;

public enum VoiceSpeed {

	/**
	 * 0.6倍速
	 */
	POINT_SIX(-2),
	POINT_EIGHT(-1),
	NORMAL(0),
	ONE_POINT_TWO(1),
	ONE_POINT_FIVE(2);
	
	private int speed;
	
	private VoiceSpeed(int speed) {
		this.speed = speed;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public String toString() {
		return String.valueOf(speed);
	}
}
