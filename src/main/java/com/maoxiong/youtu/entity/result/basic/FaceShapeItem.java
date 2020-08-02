package com.maoxiong.youtu.entity.result.basic;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author yanrun
 *
 */
public class FaceShapeItem {

	@SerializedName(value = "face_profile")
	private List<Point> faceProfile;
	@SerializedName(value = "left_eye")
	private List<Point> leftEye;
	@SerializedName(value = "right_eye")
	private List<Point> rightEye;
	@SerializedName(value = "left_eyebrow")
	private List<Point > leftEyebrow;
	@SerializedName(value = "right_eyebrow")
	private List<Point> rightEyebrow;
	private List<Point> nose;
	private List<Point> mouth;
	private List<Point> pupil;
	
	public List<Point> getLeftEye() {
		return leftEye;
	}

	public void setLeftEye(List<Point> leftEye) {
		this.leftEye = leftEye;
	}

	public List<Point> getRightEye() {
		return rightEye;
	}

	public void setRightEye(List<Point> rightEye) {
		this.rightEye = rightEye;
	}

	public List<Point> getLeftEyebrow() {
		return leftEyebrow;
	}

	public void setLeftEyebrow(List<Point> leftEyebrow) {
		this.leftEyebrow = leftEyebrow;
	}

	public List<Point> getRightEyebrow() {
		return rightEyebrow;
	}

	public void setRightEyebrow(List<Point> rightEyebrow) {
		this.rightEyebrow = rightEyebrow;
	}

	public List<Point> getNose() {
		return nose;
	}

	public void setNose(List<Point> nose) {
		this.nose = nose;
	}

	public List<Point> getMouth() {
		return mouth;
	}

	public void setMouth(List<Point> mouth) {
		this.mouth = mouth;
	}

	public List<Point> getPupil() {
		return pupil;
	}

	public void setPupil(List<Point> pupil) {
		this.pupil = pupil;
	}

	public List<Point> getFaceProfile() {
		return faceProfile;
	}

	public void setFaceProfile(List<Point> faceProfile) {
		this.faceProfile = faceProfile;
	}
	
	
}
