package com.ls.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ls.bean.NewsEntity;
import com.ls.bean.WeatherEntity;
import com.ls.tool.Contants;

public class DB {
	// ������̬ȫ�ֱ���
	static Connection conn;

	static Statement st;

	/*
	 * String table, String title, String sourceUrl, String publishTime, String
	 * clicks, String picOneUrl, String picTwoUrl, String picThreeUrl
	 */
	/* �������ݼ�¼���������������ݼ�¼�� */
	public static void insert(NewsEntity newsEntity, String table) {
		conn = getConnection(); // ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		try {
			String sql = "INSERT INTO "
					+ table
					+ "(title, sourceUrl,publishTime,clicks, picOneUrl, picTwoUrl,picThreeUrl)"
					+ " VALUES ('" + newsEntity.getTitle() + "','"
					+ newsEntity.getSuorceUrl() + "', '"
					+ newsEntity.getPublishTime() + "', '"
					+ newsEntity.getClicks() + "', '"
					+ newsEntity.getPicOneUrl() + "','"
					+ newsEntity.getPicTwoUrl() + "','"
					+ newsEntity.getPicThereUrl() + "') "; // �������ݵ�sql���

			st = conn.createStatement(); // ��������ִ�о�̬sql����Statement����

			int count = st.executeUpdate(sql); // ִ�в��������sql��䣬�����ز������ݵĸ���

			System.out.println("��staff���в��� " + count + " ������"); // �����������Ĵ�����

			conn.close(); // �ر����ݿ�����

		} catch (SQLException e) {
			System.out.println("��������ʧ��" + e.getMessage());
		}
	}

	/** ������������ */
	public static void insertWeather(WeatherEntity wEntity, String table) {
		conn = getConnection(); // ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		try {
			String sql = "INSERT INTO "
					+ table
					+ "(city, updateTime,temper,wind, moisture, dressing,sports,oneDay,twoDay,threeDay,fourDay,fiveDay)"
					+ " VALUES ('" + wEntity.getCity() + "','"
					+ wEntity.getUpdateTime() + "', '" + wEntity.getTemper()
					+ "', '" + wEntity.getWind() + "', '"
					+ wEntity.getMoisture() + "','" + wEntity.getDressing()
					+ "','" + wEntity.getSports() + "','" + wEntity.getOneDay()
					+ "','" + wEntity.getTwoDay() + "','"
					+ wEntity.getThreeDay() + "','" + wEntity.getFourDay()
					+ "','" + wEntity.getFiveDay() + "') ";
			// �������ݵ�sql���

			st = conn.createStatement(); // ��������ִ�о�̬sql����Statement����

			int count = st.executeUpdate(sql); // ִ�в��������sql��䣬�����ز������ݵĸ���

			System.out.println("��staff���в��� " + count + " ������"); // �����������Ĵ�����

			conn.close(); // �ر����ݿ�����

		} catch (SQLException e) {
			System.out.println("��������ʧ��" + e.getMessage());
		}
	}

	/* ���·���Ҫ��ļ�¼�������ظ��µļ�¼��Ŀ */
	public static void update() {
		conn = getConnection(); // ͬ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		try {
			String sql = "update staff set wage='2200' where name = 'lucy'";// �������ݵ�sql���

			st = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����st���ֲ�����

			int count = st.executeUpdate(sql);// ִ�и��²�����sql��䣬���ظ������ݵĸ���

			System.out.println("staff���и��� " + count + " ������"); // ������²����Ĵ�����

			conn.close(); // �ر����ݿ�����

		} catch (SQLException e) {
			System.out.println("��������ʧ��");
		}
	}

	/* ��ѯ���� */
	public static List<String> query(String table) {

		conn = getConnection(); // ͬ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		List<String> sourceUrl = new ArrayList<String>();
		try {
			String sql = "select  sourceUrl  from " + table
					+ " order by sourceUrl DESC"; // ��ѯ���ݵ�sql��併������
			st = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����st���ֲ�����

			ResultSet rs = st.executeQuery(sql); // ִ��sql��ѯ��䣬���ز�ѯ���ݵĽ����
			System.out.println("���Ĳ�ѯ���Ϊ��");
			while (rs.next()) { // �ж��Ƿ�����һ������
				String url = rs.getString("sourceUrl");
				System.out.println("DB:" + url);
				sourceUrl.add(url);
			}
			conn.close(); // �ر����ݿ�����
			rs.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("��ѯ����ʧ��" + e);
		}
		return sourceUrl;
	}

	/** ��ѯ������������ **/
	public static WeatherEntity queryWeather(String city) {

		conn = getConnection(); // ͬ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		WeatherEntity wEntity = null;
		try {
			String sql = "select   *   from " + " weather " + " where city = '"
					+ city + "' order by id DESC  LIMIT 1"; // ��ѯ���ݵ�sql��併������
			st = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����st���ֲ�����
			ResultSet rs = st.executeQuery(sql); // ִ��sql��ѯ��䣬���ز�ѯ���ݵĽ����
			System.out.println("����������ѯ���Ϊ��");
			while (rs.next()) { // �ж��Ƿ�����һ������
				wEntity = new WeatherEntity(rs.getString("city"),
						rs.getString("updateTime"), rs.getString("temper"),
						rs.getString("wind"), rs.getString("moisture"),
						rs.getString("dressing"), rs.getString("sports"),
						rs.getString("oneDay"), rs.getString("twoDay"),
						rs.getString("threeDay"), rs.getString("fourDay"),
						rs.getString("fiveDay"));
				System.out.println(wEntity.toString());

			}
			conn.close(); // �ر����ݿ�����
			rs.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("��ѯ����ʧ��" + e);
		}
		return wEntity;
	}

	/* ��ѯ���ݿ⣬�������Ҫ��ļ�¼����� */
	public static List<NewsEntity> queryAll(String table) {

		conn = getConnection(); // ͬ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		if (conn == null) {
			System.out.println("connweikong");
		}
		List<NewsEntity> newsEntities = new ArrayList<NewsEntity>();
		try {
			String sql = "select * from " + table + " order by sourceUrl desc"; // ��ѯ���ݵ�sql��併������
			st = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����st���ֲ�����

			ResultSet rs = st.executeQuery(sql); // ִ��sql��ѯ��䣬���ز�ѯ���ݵĽ����
			System.out.println("���Ĳ�ѯ���Ϊ��");
			while (rs.next()) { // �ж��Ƿ�����һ������

				// �����ֶ�����ȡ��Ӧ��ֵ
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String suorceUrl = rs.getString("sourceUrl");
				String publishTime = rs.getString("publishTime");
				String clicks = rs.getString("clicks");
				String picOneUrl = rs.getString("picOneUrl");
				String picTwoUrl = rs.getString("picTwoUrl");
				String picThereUrl = rs.getString("picThreeUrl");
				NewsEntity newsEntity = NewsEntity.getInstance(title,
						suorceUrl, publishTime, clicks, picOneUrl, picTwoUrl,
						picThereUrl);
				newsEntities.add(newsEntity);
				// ����鵽�ļ�¼�ĸ����ֶε�ֵ
				// System.out.println(id + " " + title + " " + suorceUrl + " "
				// + publishTime + " " + clicks + " " + picOneUrl + " "
				// + picTwoUrl + " " + picThereUrl);
				System.out.println("DB:" + publishTime);
			}
			conn.close(); // �ر����ݿ�����
			rs.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("��ѯ����ʧ��");
		}
		return newsEntities;
	}

	/* ɾ������Ҫ��ļ�¼�������� */
	public static void delete() {

		conn = getConnection(); // ͬ����Ҫ��ȡ���ӣ������ӵ����ݿ�
		try {
			String sql = "delete from staff  where name = 'lili'";// ɾ�����ݵ�sql���
			st = (Statement) conn.createStatement(); // ��������ִ�о�̬sql����Statement����st���ֲ�����

			int count = st.executeUpdate(sql);// ִ��sqlɾ����䣬����ɾ�����ݵ�����

			System.out.println("staff����ɾ�� " + count + " ������\n"); // ���ɾ�������Ĵ�����

			conn.close(); // �ر����ݿ�����

		} catch (SQLException e) {
			System.out.println("ɾ������ʧ��");
		}

	}

	/* ��ȡ���ݿ����ӵĺ��� */
	public static Connection getConnection() {
		Connection con = null; // ���������������ݿ��Connection����
		try {
			Class.forName("com.mysql.jdbc.Driver");// ����Mysql��������

			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/cquptnews", "root", "root");// ������������

		} catch (Exception e) {
			System.out.println("���ݿ�����ʧ��" + e.getMessage());
		}
		return con; // ���������������ݿ�����
	}
	//
	// public static void main(String[] args) {
	// query(Contants.TABLE_NAME[0]);
	// }
}
