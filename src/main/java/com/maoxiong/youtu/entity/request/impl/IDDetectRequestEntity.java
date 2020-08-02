package com.maoxiong.youtu.entity.request.impl;

import com.maoxiong.youtu.enums.CardType;
import com.maoxiong.youtu.entity.request.AbstractRequestEntity;

/**
 * 
 * @author yanrun
 *
 */
public class IDDetectRequestEntity extends AbstractRequestEntity {
	
	private CardType cardType;
	private boolean borderCheckFlag;

	public IDDetectRequestEntity(CardType cardType, boolean borderCheckFlag) {
		this.cardType = cardType;
		this.borderCheckFlag = borderCheckFlag;
	}
	
	@Override
	public String getFileUrl() {
		return fileUrl;
	}
	
	@Override
	public void setFileUrl(String url) {
		this.fileUrl = url;
	}
	
	@Override
	public String getFilePath() {
		return filePath;
	}
	
	@Override
	public void setFilePath(String path) {
		this.filePath = path;
	}
	
	public CardType getCardType() {
		return cardType;
	}
	
	public boolean getBorderCheckFlag() {
		return borderCheckFlag;
	}

}
