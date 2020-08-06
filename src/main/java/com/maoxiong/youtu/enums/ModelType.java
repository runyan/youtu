package com.maoxiong.youtu.enums;

/**
 * 
 * @author yanrun
 *
 */
public enum ModelType {

	/**
	 * 女声
	 */
	FEMALE(0), 
	/**
	 * 女声-英语
	 */
	FEMALE_ENGLISH(1),
	/**
	 * 男声
	 */
	MALE(2), 
	/**
	 * 喜道
	 */
	XIDAO(6);

	private int modeType;

	private ModelType(int modeType) {
		this.modeType = modeType;
	}

	public int getModelType() {
		return modeType;
	}

	@Override
	public String toString() {
		return String.valueOf(modeType);
	}
}
