package com.maoxiong.youtu.enums;

/**
 * 
 * @author yanrun
 *
 */
public enum VoiceSpeed {

	/**
	 * 0.6倍速
	 */
	POINT_SIX(-2), POINT_EIGHT(-1), NORMAL(0), ONE_POINT_TWO(1), ONE_POINT_FIVE(2);

	private int speed;

	private VoiceSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
	}

	@Override
	public String toString() {
		return String.valueOf(speed);
	}
}
