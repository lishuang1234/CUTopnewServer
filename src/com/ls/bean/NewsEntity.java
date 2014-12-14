package com.ls.bean;

public class NewsEntity {
	@Override
	public String toString() {
		return "NewsEntity [suorceUrl=" + sourceUrl + ", title=" + title
				+ ", picOneUrl=" + picOneUrl + ", picTwoUrl=" + picTwoUrl
				+ ", picThereUrl=" + picThereUrl + ", publishTime="
				+ publishTime + ", clickTimes=" + clicks + "]";
	}

	public String getSuorceUrl() {
		return sourceUrl;
	}

	public void setSuorceUrl(String suorceUrl) {
		this.sourceUrl = suorceUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicOneUrl() {
		return picOneUrl;
	}

	public void setPicOneUrl(String picOneUrl) {
		this.picOneUrl = picOneUrl;
	}

	public String getPicTwoUrl() {
		return picTwoUrl;
	}

	public void setPicTwoUrl(String picTwoUrl) {
		this.picTwoUrl = picTwoUrl;
	}

	public String getPicThereUrl() {
		return picThereUrl;
	}

	public void setPicThereUrl(String picThereUrl) {
		this.picThereUrl = picThereUrl;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getClicks() {
		return clicks;
	}

	public void setClicks(String clicks) {
		this.clicks = clicks;
	}

	private NewsEntity(String title, String suorceUrl, String publishTime,
			String clicks, String picOneUrl, String picTwoUrl,
			String picThereUrl) {
		super();
		this.title = title;
		this.sourceUrl = suorceUrl;
		this.publishTime = publishTime;
		this.clicks = clicks;
		this.picOneUrl = picOneUrl;
		this.picTwoUrl = picTwoUrl;
		this.picThereUrl = picThereUrl;
	}

	public static NewsEntity getInstance(String title, String suorceUrl,
			String publishTime, String clicks, String picOneUrl,
			String picTwoUrl, String picThereUrl) {
		return new NewsEntity(title, suorceUrl, publishTime, clicks, picOneUrl,
				picTwoUrl, picThereUrl);
	}

	private String title;
	private String sourceUrl;
	private String publishTime;
	private String clicks;
	private String picOneUrl;
	private String picTwoUrl;
	private String picThereUrl;

}
