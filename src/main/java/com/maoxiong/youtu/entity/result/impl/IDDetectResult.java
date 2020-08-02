package com.maoxiong.youtu.entity.result.impl;

import com.google.gson.annotations.SerializedName;
import com.maoxiong.youtu.entity.result.BaseResult;

/**
 * 
 * @author yanrun
 *
 */
public class IDDetectResult extends BaseResult {
	
	private String name;
	@SerializedName("name_confidence_all")
	private int[] nameConfidenceAll ;
	private String sex;
	@SerializedName("sex_confidence_all")
	private int[] sexConfidenceAll;
	private String nation;
	@SerializedName("nation_confidence_all")
	private int[] nationConfidenceAll;
	private String birth;
	@SerializedName("birth_confidence_all")
	private int[] birthConfidenceAll;
	private String address;
	@SerializedName("address_confidence_all") 
	private int[] addressConfidenceAll;
	private String id;
	@SerializedName("id_confidence_all") 
	private int[] idConfidenceAll;
	private String frontimage;
	@SerializedName("frontimage_confidence_all") 
	private int[] frontimageConfidenceAll;
	@SerializedName("watermask_status")
	private int watermaskStatus; 
	@SerializedName("watermask_confidence_all")
	private int[] watermaskConfidenceAll;
	@SerializedName("valid_date")
	private String validDate; 
	@SerializedName("valid_date_confidence_all")
	private int[] validDateConfidenceAll;
	private String authority;
	@SerializedName("authority_confidence_all")
	private int[] authorityConfidenceAll;
	private String backimage;
	@SerializedName("backimage_confidence_all")
	private int[] backimageConfidenceAll;
	@SerializedName("detail_errorcode") 
	private int[] detailErrorcode;
	@SerializedName("detail_errormsg") 
	private String[] detailErrormsg;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int[] getNameConfidenceAll() {
		return nameConfidenceAll;
	}
	public void setNameConfidenceAll(int[] nameConfidenceAll) {
		this.nameConfidenceAll = nameConfidenceAll;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int[] getSexConfidenceAll() {
		return sexConfidenceAll;
	}
	public void setSexConfidenceAll(int[] sexConfidenceAll) {
		this.sexConfidenceAll = sexConfidenceAll;
	}
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public int[] getNationConfidenceAll() {
		return nationConfidenceAll;
	}
	public void setNationConfidenceAll(int[] nationConfidenceAll) {
		this.nationConfidenceAll = nationConfidenceAll;
	}
	public String getBirth() {
		return birth;
	}
	public void setBirth(String birth) {
		this.birth = birth;
	}
	public int[] getBirthConfidenceAll() {
		return birthConfidenceAll;
	}
	public void setBirthConfidenceAll(int[] birthConfidenceAll) {
		this.birthConfidenceAll = birthConfidenceAll;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int[] getAddressConfidenceAll() {
		return addressConfidenceAll;
	}
	public void setAddressConfidenceAll(int[] addressConfidenceAll) {
		this.addressConfidenceAll = addressConfidenceAll;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int[] getIdConfidenceAll() {
		return idConfidenceAll;
	}
	public void setIdConfidenceAll(int[] idConfidenceAll) {
		this.idConfidenceAll = idConfidenceAll;
	}
	public String getFrontimage() {
		return frontimage;
	}
	public void setFrontimage(String frontimage) {
		this.frontimage = frontimage;
	}
	public int[] getFrontimageConfidenceAll() {
		return frontimageConfidenceAll;
	}
	public void setFrontimageConfidenceAll(int[] frontimageConfidenceAll) {
		this.frontimageConfidenceAll = frontimageConfidenceAll;
	}
	public int getWatermaskStatus() {
		return watermaskStatus;
	}
	public void setWatermaskStatus(int watermaskStatus) {
		this.watermaskStatus = watermaskStatus;
	}
	public int[] getWatermaskConfidenceAll() {
		return watermaskConfidenceAll;
	}
	public void setWatermaskConfidenceAll(int[] watermaskConfidenceAll) {
		this.watermaskConfidenceAll = watermaskConfidenceAll;
	}
	public String getValidDate() {
		return validDate;
	}
	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}
	public int[] getValidDateConfidenceAll() {
		return validDateConfidenceAll;
	}
	public void setValidDateConfidenceAll(int[] validDateConfidenceAll) {
		this.validDateConfidenceAll = validDateConfidenceAll;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public int[] getAuthorityConfidenceAll() {
		return authorityConfidenceAll;
	}
	public void setAuthorityConfidenceAll(int[] authorityConfidenceAll) {
		this.authorityConfidenceAll = authorityConfidenceAll;
	}
	public String getBackimage() {
		return backimage;
	}
	public void setBackimage(String backimage) {
		this.backimage = backimage;
	}
	public int[] getBackimageConfidenceAll() {
		return backimageConfidenceAll;
	}
	public void setBackimageConfidenceAll(int[] backimageConfidenceAll) {
		this.backimageConfidenceAll = backimageConfidenceAll;
	}
	public int[] getDetailErrorcode() {
		return detailErrorcode;
	}
	public void setDetailErrorcode(int[] detailErrorcode) {
		this.detailErrorcode = detailErrorcode;
	}
	public String[] getDetailErrormsg() {
		return detailErrormsg;
	}
	public void setDetailErrormsg(String[] detailErrormsg) {
		this.detailErrormsg = detailErrormsg;
	}
	
}
