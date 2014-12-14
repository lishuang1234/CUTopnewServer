package com.ls.tool;

import com.alibaba.fastjson.JSON;

public class JsonTool {

	public static String createJsonString(Object object) {
		String jsonString = "";
		try {
			jsonString = JSON.toJSONString(object);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return jsonString;
	}
}
