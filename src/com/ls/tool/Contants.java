package com.ls.tool;

public class Contants {
	public static final String[] TABLE_NAME = { "topnews", "academynews",
			"classnews", "medianews", "specialnews" };
	public static final int[] NEWS_URL_ID = { 1, 3, 4, 6, 7 };
	public static final String[] REFRESH = { "newsReAll", "weatherRe" };
	public static String[] cityArray = new String[] { "chongqing", "shanghai",
			"beijing" };
	public static String[] cityNameArray = new String[] { "重庆", "上海", "北京" };

	public static String getCityName(String city) {
		for (int i = 0; i < cityArray.length; i++) {
			if (cityArray[i].equals(city)) {
				System.out.println("匹配结果：" + cityNameArray[i]);
				return cityNameArray[i];
			}
		}
		return null;
	}
}
