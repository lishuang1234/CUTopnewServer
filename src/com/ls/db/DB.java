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
	// 创建静态全局变量
	static Connection conn;

	static Statement st;

	/*
	 * String table, String title, String sourceUrl, String publishTime, String
	 * clicks, String picOneUrl, String picTwoUrl, String picThreeUrl
	 */
	/* 插入数据记录，并输出插入的数据记录数 */
	public static void insert(NewsEntity newsEntity, String table) {
		conn = getConnection(); // 首先要获取连接，即连接到数据库
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
					+ newsEntity.getPicThereUrl() + "') "; // 插入数据的sql语句

			st = conn.createStatement(); // 创建用于执行静态sql语句的Statement对象

			int count = st.executeUpdate(sql); // 执行插入操作的sql语句，并返回插入数据的个数

			System.out.println("向staff表中插入 " + count + " 条数据"); // 输出插入操作的处理结果

			conn.close(); // 关闭数据库连接

		} catch (SQLException e) {
			System.out.println("插入数据失败" + e.getMessage());
		}
	}

	/** 插入天气数据 */
	public static void insertWeather(WeatherEntity wEntity, String table) {
		conn = getConnection(); // 首先要获取连接，即连接到数据库
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
			// 插入数据的sql语句

			st = conn.createStatement(); // 创建用于执行静态sql语句的Statement对象

			int count = st.executeUpdate(sql); // 执行插入操作的sql语句，并返回插入数据的个数

			System.out.println("向staff表中插入 " + count + " 条数据"); // 输出插入操作的处理结果

			conn.close(); // 关闭数据库连接

		} catch (SQLException e) {
			System.out.println("插入数据失败" + e.getMessage());
		}
	}

	/* 更新符合要求的记录，并返回更新的记录数目 */
	public static void update() {
		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		try {
			String sql = "update staff set wage='2200' where name = 'lucy'";// 更新数据的sql语句

			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量

			int count = st.executeUpdate(sql);// 执行更新操作的sql语句，返回更新数据的个数

			System.out.println("staff表中更新 " + count + " 条数据"); // 输出更新操作的处理结果

			conn.close(); // 关闭数据库连接

		} catch (SQLException e) {
			System.out.println("更新数据失败");
		}
	}

	/* 查询标题 */
	public static List<String> query(String table) {

		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		List<String> sourceUrl = new ArrayList<String>();
		try {
			String sql = "select  sourceUrl  from " + table
					+ " order by sourceUrl DESC"; // 查询数据的sql语句降序排列
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量

			ResultSet rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集
			System.out.println("最后的查询结果为：");
			while (rs.next()) { // 判断是否还有下一个数据
				String url = rs.getString("sourceUrl");
				System.out.println("DB:" + url);
				sourceUrl.add(url);
			}
			conn.close(); // 关闭数据库连接
			rs.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("查询数据失败" + e);
		}
		return sourceUrl;
	}

	/** 查询最新天气数据 **/
	public static WeatherEntity queryWeather(String city) {

		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		WeatherEntity wEntity = null;
		try {
			String sql = "select   *   from " + " weather " + " where city = '"
					+ city + "' order by id DESC  LIMIT 1"; // 查询数据的sql语句降序排列
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量
			ResultSet rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集
			System.out.println("最后的天气查询结果为：");
			while (rs.next()) { // 判断是否还有下一个数据
				wEntity = new WeatherEntity(rs.getString("city"),
						rs.getString("updateTime"), rs.getString("temper"),
						rs.getString("wind"), rs.getString("moisture"),
						rs.getString("dressing"), rs.getString("sports"),
						rs.getString("oneDay"), rs.getString("twoDay"),
						rs.getString("threeDay"), rs.getString("fourDay"),
						rs.getString("fiveDay"));
				System.out.println(wEntity.toString());

			}
			conn.close(); // 关闭数据库连接
			rs.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("查询数据失败" + e);
		}
		return wEntity;
	}

	/* 查询数据库，输出符合要求的记录的情况 */
	public static List<NewsEntity> queryAll(String table) {

		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		if (conn == null) {
			System.out.println("connweikong");
		}
		List<NewsEntity> newsEntities = new ArrayList<NewsEntity>();
		try {
			String sql = "select * from " + table + " order by sourceUrl desc"; // 查询数据的sql语句降序排列
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量

			ResultSet rs = st.executeQuery(sql); // 执行sql查询语句，返回查询数据的结果集
			System.out.println("最后的查询结果为：");
			while (rs.next()) { // 判断是否还有下一个数据

				// 根据字段名获取相应的值
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
				// 输出查到的记录的各个字段的值
				// System.out.println(id + " " + title + " " + suorceUrl + " "
				// + publishTime + " " + clicks + " " + picOneUrl + " "
				// + picTwoUrl + " " + picThereUrl);
				System.out.println("DB:" + publishTime);
			}
			conn.close(); // 关闭数据库连接
			rs.close();
			st.close();
		} catch (SQLException e) {
			System.out.println("查询数据失败");
		}
		return newsEntities;
	}

	/* 删除符合要求的记录，输出情况 */
	public static void delete() {

		conn = getConnection(); // 同样先要获取连接，即连接到数据库
		try {
			String sql = "delete from staff  where name = 'lili'";// 删除数据的sql语句
			st = (Statement) conn.createStatement(); // 创建用于执行静态sql语句的Statement对象，st属局部变量

			int count = st.executeUpdate(sql);// 执行sql删除语句，返回删除数据的数量

			System.out.println("staff表中删除 " + count + " 条数据\n"); // 输出删除操作的处理结果

			conn.close(); // 关闭数据库连接

		} catch (SQLException e) {
			System.out.println("删除数据失败");
		}

	}

	/* 获取数据库连接的函数 */
	public static Connection getConnection() {
		Connection con = null; // 创建用于连接数据库的Connection对象
		try {
			Class.forName("com.mysql.jdbc.Driver");// 加载Mysql数据驱动

			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/cquptnews", "root", "root");// 创建数据连接

		} catch (Exception e) {
			System.out.println("数据库连接失败" + e.getMessage());
		}
		return con; // 返回所建立的数据库连接
	}
	//
	// public static void main(String[] args) {
	// query(Contants.TABLE_NAME[0]);
	// }
}
