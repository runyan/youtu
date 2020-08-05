package com.maoxiong.youtu.entity.result.basic;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author yanrun
 *
 */
public class InvoiceItem {

	private String item;
	@SerializedName(value = "itemstring")
	private String itemString;
	@SerializedName(value = "itemconf")
	private double itemConf;
	@SerializedName(value = "itemcoord")
	private ItemCor cor;
	private List<String> candword;
	private List<Float> coords;
	private List<Word> words;

	public List<String> getCandword() {
		return candword;
	}

	public void setCandword(List<String> candword) {
		this.candword = candword;
	}

	public List<Float> getCoords() {
		return coords;
	}

	public void setCoords(List<Float> coords) {
		this.coords = coords;
	}

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

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

	@Override
	public String toString() {
		return "InvoiceItem [item=" + item + ", itemString=" + itemString + ", itemConf=" + itemConf + ", cor=" + cor
				+ ", candword=" + candword + ", coords=" + coords + ", words=" + words + "]";
	}

}
