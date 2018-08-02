package com.maoxiong.youtu.entity.result.impl;

import com.google.gson.annotations.SerializedName;
import com.maoxiong.youtu.entity.result.BaseResult;

/**
 * 
 * @author yanrun
 *
 */
public class FoodDetectResult extends BaseResult {

	private boolean food;
	@SerializedName(value = "food_confidence")
	private double foodConfidence;
	
	public boolean isFood() {
		return food;
	}
	public void setFood(boolean food) {
		this.food = food;
	}
	public double getFoodConfidence() {
		return foodConfidence;
	}
	public void setFoodConfidence(double foodConfidence) {
		this.foodConfidence = foodConfidence;
	}
	
}
