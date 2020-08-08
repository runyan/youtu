package com.maoxiong.youtu.util.configloader.entity;

import java.io.Serializable;

/**
 * 
 * @author yanrun
 *
 */
public class JsonConfigEntity implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2827471642295492944L;
	
	private String configName;
	private String configValue;
	
	public String getConfigName() {
		return configName;
	}
	public void setConfigName(String configName) {
		this.configName = configName;
	}
	public String getConfigValue() {
		return configValue;
	}
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((configName == null) ? 0 : configName.hashCode());
		result = prime * result + ((configValue == null) ? 0 : configValue.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JsonConfigEntity other = (JsonConfigEntity) obj;
		if (configName == null) {
			if (other.configName != null)
				return false;
		} else if (!configName.equals(other.configName))
			return false;
		if (configValue == null) {
			if (other.configValue != null)
				return false;
		} else if (!configValue.equals(other.configValue))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "JsonConfigEntity [configName=" + configName + ", configValue=" + configValue + "]";
	}
}
