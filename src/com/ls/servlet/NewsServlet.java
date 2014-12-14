package com.ls.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ls.bean.WeatherEntity;
import com.ls.db.DB;
import com.ls.tool.Contants;
import com.ls.tool.JsonTool;
import com.ls.tool.JsoupNews;
import com.ls.tool.Weathertool;

public class NewsServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4760528045192771335L;
	private Timer mTimer;
	private TimerTask mTask;

	/**
	 * Constructor of the object.
	 */
	public NewsServlet() {
		super();

	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String action_flag = request.getParameter("action_flag");
		String update_url = request.getParameter("url");
		String update_city = request.getParameter("city");
		String newsEntity = null;
		String weatherEntityString = null;// 天气信息Json数据
		PrintWriter out = response.getWriter();
		System.out.println("action_flag:" + action_flag + " update_url:"
				+ update_url + "update_city" + update_city);
		if (action_flag.equals(Contants.TABLE_NAME[0])) {// topnews
			if (update_url != null) {// 处理更新
				newsEntity = JsonTool.createJsonString(JsoupNews
						.upDateNewsFromDBById(0, update_url));
				System.out.println("更新新闻！" + Contants.TABLE_NAME[0]);
			} else {
				newsEntity = JsonTool.createJsonString(DB
						.queryAll(Contants.TABLE_NAME[0]));
			}
			out.println(newsEntity);
		} else if (action_flag.equals(Contants.TABLE_NAME[1])) {// academynews
			if (update_url != null) {// 处理更新
				newsEntity = JsonTool.createJsonString(JsoupNews
						.upDateNewsFromDBById(1, update_url));
				System.out.println("更新新闻！" + Contants.TABLE_NAME[1]);
			} else {
				newsEntity = JsonTool.createJsonString(DB
						.queryAll(Contants.TABLE_NAME[1]));
			}
			out.println(newsEntity);
		} else if (action_flag.equals(Contants.TABLE_NAME[2])) {// classnews
			if (update_url != null) {// 处理更新
				newsEntity = JsonTool.createJsonString(JsoupNews
						.upDateNewsFromDBById(2, update_url));
				System.out.println("更新新闻！" + Contants.TABLE_NAME[2]);
			} else {
				newsEntity = JsonTool.createJsonString(DB
						.queryAll(Contants.TABLE_NAME[2]));
			}
			out.println(newsEntity);

		} else if (action_flag.equals(Contants.TABLE_NAME[3])) {// medianews
			if (update_url != null) {// 处理更新
				newsEntity = JsonTool.createJsonString(JsoupNews
						.upDateNewsFromDBById(3, update_url));
				System.out.println("更新新闻！" + Contants.TABLE_NAME[3]);
			} else {
				newsEntity = JsonTool.createJsonString(DB
						.queryAll(Contants.TABLE_NAME[3]));
			}
			out.println(newsEntity);
		} else if (action_flag.equals(Contants.TABLE_NAME[4])) {// specialnews
			if (update_url != null) {// 处理更新
				newsEntity = JsonTool.createJsonString(JsoupNews
						.upDateNewsFromDBById(4, update_url));
				System.out.println("更新新闻！" + Contants.TABLE_NAME[4]);
			} else {
				newsEntity = JsonTool.createJsonString(DB
						.queryAll(Contants.TABLE_NAME[4]));
			}
			out.println(newsEntity);
		} else if (action_flag.equals(Contants.REFRESH[0])) {// 刷新全部
			JsoupNews.upDateAllNews();
		} else if (action_flag.endsWith(Contants.REFRESH[1])) {// 更新天气
			if (update_city != null) {
				System.out.println("获取天气！");
				WeatherEntity weatherEntity = null;
				String city = Contants.getCityName(update_city);
				if (city == null) {
					out.println("Error!");
					out.flush();
					out.close();
					return;
				}
				weatherEntity = DB.queryWeather(city);// 去查询数据库天气数据
				if (weatherEntity == null) {// 数据库沒有数据
					weatherEntity = Weathertool.getWeather(city);
					if (weatherEntity != null)
						DB.insertWeather(weatherEntity, "weather");
				}
				weatherEntityString = JsonTool.createJsonString(weatherEntity);
				System.out.println(weatherEntityString);
			}
			out.println(weatherEntityString);
		} else {
			out.println("Error!");
		}

		out.flush();
		out.close();
	}

	@Override
	public void init() throws ServletException {
		System.out.println("init");
		// TODO Auto-generated method stub
		mTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("timer update plan !");
				JsoupNews.upDateAllNews();
				Weathertool.updateWeather("重庆");
			}
		};
		mTimer = new Timer();
		mTimer.schedule(mTask, 1000, 1000 * 60 * 60 * 1 / 2);
		super.init();
	}

}
