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
		// DB.insertWeather(getWeather("�Ϻ�"), "weather");
		// DB.queryWeather("�Ϻ�");
//		System.out.println(getWeather("����"));
		// getWeather("����");
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
	 * �û���SOAP�����͸��������ˣ������ط������㷵�ص�������
	 * 
	 * @param city
	 *            �û�����ĳ�������
	 * @return �������˷��ص������������ͻ��˶�ȡ
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
	 * �Է������˷��ص�XML���н���
	 * 
	 * @param city
	 *            �û�����ĳ�������
	 * @return �ַ��� ��#�ָ�
	 */
	public static WeatherEntity getWeather(String cityName) {
		try {
			Document doc;
			WeatherEntity wEntity;
			String city = "";// ����
			String updateTime = "";// ����ʱ��
			String temper = "";// �¶�
			String wind = "";// ����
			String moisture = "";// ʪ��
			String dressing = "";// ����ָ��
			String sports = "";// �˶�ָ��
			String oneDay = "";// ��������
			String twoDay = "";// �ڶ���
			String threeDay = "";// ������
			String fourDay = "";// ������
			String fiveDay = "";// ������
			String tempArray[] = new String[20];
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = getSoapInputStream(cityName);
			doc = db.parse(is);
			NodeList nl = doc.getElementsByTagName("string");// ���е� String�ڵ�
			if (nl == null) {
				return null;
			}
			city = nl.item(1).getFirstChild().getNodeValue();
			System.out.println(city);
			updateTime = nl.item(3).getFirstChild().getNodeValue();
			tempArray = (nl.item(4).getFirstChild().getNodeValue()).split("��");
			if (tempArray.length > 1) {// ��ȡ�����ſ�
				temper = tempArray[0];
				wind = tempArray[1];
				moisture = tempArray[2];
			}
			tempArray = nl.item(6).getFirstChild().getNodeValue().split("��");
			if (tempArray.length > 1) {// ��ȡ����ָ�����˶�ָ��
				dressing = tempArray[2] + "��" + tempArray[3] + "��";
				sports = tempArray[6] + "��" + tempArray[7] + "��";
			}
			tempArray = new String[] { "", "", "", "", "" };
			int k = 7;
			for (int j = 0; j < 5; j++) {// ��ȡ������������Ϣ
				for (int i = 0; i < 5; i++) {
					tempArray[j] = tempArray[j]
							+ nl.item(k).getFirstChild().getNodeValue() + "��";
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
			// if (n.getFirstChild().getNodeValue().equals("��ѯ���Ϊ�գ�")) {
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

	/** ������������ */
	public static void updateWeather(String city) {
		WeatherEntity wEntity = getWeather(city);
		if (wEntity != null)
			DB.insertWeather(wEntity, "weather");
	}
}
