package com.h3c.o2o.portal.common;

public class HttpOrHttpsMgrImpl implements HttpOrHttpsMgr{
	private String httpOrHttps;

	@Override
	public String getHttpOrHttps() {
		return httpOrHttps;
	}

	public void setHttpOrHttps(String httpOrHttps) {
		this.httpOrHttps = httpOrHttps;
	}
}
