package com.maoxiong.youtu.enums;

/**
 * 
 * @author yanrun
 *
 */
public enum ModelType {

	FEMALE(0), FEMALE_ENGLISH(1), MALE(2), XIDAO(6);

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
