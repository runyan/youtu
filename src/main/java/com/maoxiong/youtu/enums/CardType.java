package com.maoxiong.youtu.enums;

/**
 * 
 * @author yanrun
 *
 */
public enum CardType {

	/**
	 * front
	 */
	FRONT(0),
	/**
	 * back
	 */
	BACK(1);

	private int cardType;

	private CardType(int type) {
		this.cardType = type;
	}

	public int getCardTypeValue() {
		return cardType;
	}

	@Override
	public String toString() {
		return String.valueOf(cardType);
	}
}
