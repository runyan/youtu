package com.maoxiong.youtu.entity.request.impl;

import com.maoxiong.youtu.entity.request.AbstractRequestEntity;

/**
 * 
 * @author yanrun
 *
 */
public class CreditCradDetectRequestEntity extends AbstractRequestEntity {

	@Override
	public String getFilePath() {
		return filePath;
	}

	@Override
	public void setFilePath(String path) {
		this.filePath = path;
	}

	@Override
	public String getFileUrl() {
		return fileUrl;
	}

	@Override
	public void setFileUrl(String url) {
		this.fileUrl = url;
	}

}
