package com.ls.tool;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.ls.bean.WeatherEntity;
import com.ls.db.DB;

public class Weathertool {
	public static void main(String[] args) {
		// DB.insertWeather(getWeather("上海"), "weather");
		// DB.queryWeather("上海");
//		System.out.println(getWeather("重庆"));
		// getWeather("重庆");
	}

	private static String getSoapRequest(String city) {
		StringBuilder sb = new StringBuilder();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>"
				+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
				+ "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
				+ "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
				+ "<soap:Body>    <getWeather xmlns=\"http://WebXml.com.cn/\">"
				+ "<theCityCode>" + city + "</theCityCode>  " + "</getWeather>"
				+ "</soap:Body></soap:Envelope>");
		return sb.toString();
	}

	/**
	 * 用户把SOAP请求发送给服务器端，并返回服务器点返回的输入流
	 * 
	 * @param city
	 *            用户输入的城市名称
	 * @return 服务器端返回的输入流，供客户端读取
	 * @throws Exception
	 */
	private static InputStream getSoapInputStream(String city) throws Exception {
		try {
			String soap = getSoapRequest(city);
			if (soap == null) {
				return null;
			}
			URL url = new URL(
					"http://www.webxml.com.cn/WebServices/WeatherWS.asmx");
			URLConnection conn = url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			conn.setRequestProperty("Content-Length",
					Integer.toString(soap.length()));
			conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
			conn.setRequestProperty("SOAPAction",
					"http://WebXml.com.cn/getWeather");

			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
			osw.write(soap);
			osw.flush();
			osw.close();

			InputStream is = conn.getInputStream();
			return is;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 对服务器端返回的XML进行解析
	 * 
	 * @param city
	 *            用户输入的城市名称
	 * @return 字符串 用#分割
	 */
	public static WeatherEntity getWeather(String cityName) {
		try {
			Document doc;
			WeatherEntity wEntity;
			String city = "";// 城市
			String updateTime = "";// 更新时间
			String temper = "";// 温度
			String wind = "";// 风向
			String moisture = "";// 湿度
			String dressing = "";// 穿衣指数
			String sports = "";// 运动指数
			String oneDay = "";// 今天气温
			String twoDay = "";// 第二天
			String threeDay = "";// 第三天
			String fourDay = "";// 第四天
			String fiveDay = "";// 第五天
			String tempArray[] = new String[20];
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = getSoapInputStream(cityName);
			doc = db.parse(is);
			NodeList nl = doc.getElementsByTagName("string");// 所有的 String节点
			if (nl == null) {
				return null;
			}
			city = nl.item(1).getFirstChild().getNodeValue();
			System.out.println(city);
			updateTime = nl.item(3).getFirstChild().getNodeValue();
			tempArray = (nl.item(4).getFirstChild().getNodeValue()).split("；");
			if (tempArray.length > 1) {// 获取天气概况
				temper = tempArray[0];
				wind = tempArray[1];
				moisture = tempArray[2];
			}
			tempArray = nl.item(6).getFirstChild().getNodeValue().split("。");
			if (tempArray.length > 1) {// 获取穿衣指数和运动指数
				dressing = tempArray[2] + "，" + tempArray[3] + "。";
				sports = tempArray[6] + "，" + tempArray[7] + "。";
			}
			tempArray = new String[] { "", "", "", "", "" };
			int k = 7;
			for (int j = 0; j < 5; j++) {// 获取近五天天气信息
				for (int i = 0; i < 5; i++) {
					tempArray[j] = tempArray[j]
							+ nl.item(k).getFirstChild().getNodeValue() + "，";
					k++;
				}
			}
			oneDay = tempArray[0];
			twoDay = tempArray[1];
			threeDay = tempArray[2];
			fourDay = tempArray[3];
			fiveDay = tempArray[4];
			StringBuffer sb = new StringBuffer();
			// for (int count = 0; count < nl.getLength(); count++) {
			// Node n = nl.item(count);
			// if (n.getFirstChild().getNodeValue().equals("查询结果为空！")) {
			// break;
			// }
			// System.out.println("jiedian : " + count
			// + n.getFirstChild().getNodeValue());
			//
			// sb.append(n.getFirstChild().getNodeValue());
			// }
			// is.close();
			wEntity = new WeatherEntity(city, updateTime, temper, wind,
					moisture, dressing, sports, oneDay, twoDay, threeDay,
					fourDay, fiveDay);
			System.out.println("GetWeather:" + wEntity);
			return wEntity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 更新天气数据 */
	public static void updateWeather(String city) {
		WeatherEntity wEntity = getWeather(city);
		if (wEntity != null)
			DB.insertWeather(wEntity, "weather");
	}
}
