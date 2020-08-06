package com.maoxiong.youtu.request.impl;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.maoxiong.youtu.cache.Cache;
import com.maoxiong.youtu.cache.impl.LruCache;
import com.maoxiong.youtu.constants.HttpConstants;
import com.maoxiong.youtu.entity.request.impl.InvoiceDetectRequestEntity;
import com.maoxiong.youtu.request.Request;
import com.maoxiong.youtu.util.CacheKeyUtil;
import com.maoxiong.youtu.util.FileUtil;
import com.maoxiong.youtu.util.ParamUtil;

/**
 * 
 * @author yanrun
 *
 */
public class InvoiceDetectRequest implements Request {

	private InvoiceDetectRequestEntity params;
	private String url;

	private static Cache<String, String> cache = new LruCache<>(16);

	@Override
	public void setParams(Object requestEntity) {
		this.params = (InvoiceDetectRequestEntity) requestEntity;
		this.url = HttpConstants.BASE_URL + "ocrapi/invoiceocr";
	}

	@Override
	public String getParamsJsonString() {
		String className = getClass().getName();
		String url = params.getFileUrl();
		String cacheKey = CacheKeyUtil.generateCacheKey(className, url);
		String jsonStr = cache.getIfPresent(cacheKey);
		if (StringUtils.isBlank(jsonStr)) {
			byte[] imgData = FileUtil.readFileByBytes(this.params.getFileUrl());
			String image = Base64.getEncoder().encodeToString(imgData);
			Map<String, Object> paramMap = new HashMap<>(4);
			paramMap.put("image", image);
			jsonStr = ParamUtil.getInstance().buildParamJson(paramMap);
			cache.set(cacheKey, jsonStr);
		}
		return jsonStr;
	}

	@Override
	public String getRequestUrl() {
		return this.url;
	}

}
