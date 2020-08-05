package com.maoxiong.youtu.entity.result.basic;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author yanrun
 *
 */
public class Item {

	private String item;
	@SerializedName(value = "itemstring")
	private String itemString;
	@SerializedName(value = "itemconf")
	private double itemConf;
	@SerializedName(value = "itemcoord")
	private ItemCor cor;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getItemString() {
		return itemString;
	}

	public void setItemString(String itemString) {
		this.itemString = itemString;
	}

	public double getItemConf() {
		return itemConf;
	}

	public void setItemConf(double itemConf) {
		this.itemConf = itemConf;
	}

	public ItemCor getCor() {
		return cor;
	}

	public void setCor(ItemCor cor) {
		this.cor = cor;
	}

}
