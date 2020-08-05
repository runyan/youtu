package com.maoxiong.youtu.entity.result.basic;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author yanrun
 *
 */
public class FaceItem {

	@SerializedName(value = "face_id")
	private String faceId;
	private Integer x;
	private Integer y;
	private Integer height;
	private Integer width;
	private Integer pitch;
	private Integer roll;
	private Integer yaw;
	private Integer age;
	private Integer gender;
	private boolean glass;
	private Integer mask;
	private Integer hat;
	private Integer beauty;
	@SerializedName(value = "face_shape")
	private FaceShapeItem faceShape;

	public String getFaceId() {
		return faceId;
	}

	public void setFaceId(String faceId) {
		this.faceId = faceId;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getPitch() {
		return pitch;
	}

	public void setPitch(Integer pitch) {
		this.pitch = pitch;
	}

	public Integer getRoll() {
		return roll;
	}

	public void setRoll(Integer roll) {
		this.roll = roll;
	}

	public Integer getYaw() {
		return yaw;
	}

	public void setYaw(Integer yaw) {
		this.yaw = yaw;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender == 0 ? "Female" : "Male";
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public boolean isGlass() {
		return glass;
	}

	public void setGlass(boolean glass) {
		this.glass = glass;
	}

	public Integer getMask() {
		return mask;
	}

	public void setMask(Integer mask) {
		this.mask = mask;
	}

	public Integer getHat() {
		return hat;
	}

	public void setHat(Integer hat) {
		this.hat = hat;
	}

	public Integer getBeauty() {
		return beauty;
	}

	public void setBeauty(Integer beauty) {
		this.beauty = beauty;
	}

	public FaceShapeItem getFaceShape() {
		return faceShape;
	}

	public void setFaceShape(FaceShapeItem faceShape) {
		this.faceShape = faceShape;
	}

}
