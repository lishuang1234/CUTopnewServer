package com.ls.tool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ls.bean.NewsEntity;
import com.ls.db.DB;

/** 获取新闻内容 */
public class JsoupNews {
	public static String getNewsDetails(String url) {
		Document document = null;
		String newsContent = null;
		try {
			document = Jsoup.connect(url).timeout(9000).get();
			Element element = null;
			if (url != null) {
				element = document.getElementById("news_content");
			}
			if (element != null) {
				System.out.println("element:" + element.toString());
				newsContent = element.toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newsContent;
	}

	/** 获取对应新闻标题和列表id：134679 */
	public static List<String> getNewsTitleOrUrl(int id, int page, int flag) {

		// TODO Auto-generated method stub
		String url = "http://xwzx.cqupt.edu.cn/xwzx/news_type.php?id=" + id
				+ "&page=" + page;
		Document document = null;
		List<String> news = new ArrayList<String>();
		try {
			document = Jsoup.connect(url).timeout(90000).get();
			Elements elements = document.getElementsByAttributeValue("width",
					"540");
			for (Element element : elements) {
				Elements elements2 = element.getElementsByTag("a");
				if (flag == 0) {// 标题
					System.out.println("title:" + elements2.text());
					news.add(elements2.text());
				} else if (flag == 1) {// 新闻地址
					System.out.println("url:" + elements2.attr("abs:href"));// 获取绝对地址
					news.add(elements2.attr("abs:href"));
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return news;
	}

	/** 获取新闻时间点击次数 */
	// /此处性能问题
	public static List<String> getPublishTimeOrClicks(String url, int flag) {

		// TODO Auto-generated method stub
		// String url = "http://xwzx.cqupt.edu.cn/xwzx/news.php?id=24830";
		Document document = null;
		List<String> dataList = new ArrayList<String>();
		String data = null;
		try {
			document = Jsoup.connect(url).timeout(90000).get();
			Elements elements = document.getElementsByAttributeValue("style",
					"line-height:30px;");
			for (Element element : elements) {

				data = element.text();
				if (flag == 0) {// 获取时间
					dataList.add(data.substring(3, 13));
				} else if (flag == 1) {// 点击次数
					dataList.add((data.substring(data.length() - 7,
							data.length() - 1)).split("：")[1]);
					System.out.println("time:"
							+ (data.substring(data.length() - 7,
									data.length() - 1)).split("：")[1]);
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (flag == 0) {// 获取时间
				dataList.add("2014-10-11");
			} else if (flag == 1) {// 点击次数
				dataList.add("223");
				// System.out.println("time:" + data.substring(34, 39));

			}
		}
		return dataList;

	}

	/** 获取新闻当中多张的图片供列表展示缩略图 */
	public static List<String> getNewsImages(String url) {

		// TODO Auto-generated method stub
		// String url = "http://xwzx.cqupt.edu.cn/xwzx/news.php?id=24712";
		Document document = null;
		List<String> newImagesList = new ArrayList<String>();
		try {
			document = Jsoup.connect(url).timeout(90000).get();
			Element element = document.getElementById("news_content");
			Elements e = element.select("IMG");
			for (Element e4 : e) {
				String e2 = e4.attr("abs:src");
				System.out.println("IMG:" + e2);
				if (e2 != "")
					newImagesList.add(e2);
			}
			switch (newImagesList.size()) {
			case 0:
				newImagesList.add(0, null);
				newImagesList.add(1, null);
				newImagesList.add(2, null);
				break;
			case 1:
				newImagesList.add(1, null);
				newImagesList.add(2, null);
				break;
			case 2:
				newImagesList.add(2, null);
				break;
			default:
				break;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newImagesList;
	}

	//
	// public static void main(String[] args) {
	// int[] id = { 1, 3, 4, 6, 7, 9 };
	// int[] page = { 1, 2 };
	// for (int j = 0; j < 2; j++) {
	// for (int i = 0; i < id.length; i++) {
	// if (j == 2 & i == 4) {// /specialNews 第二页不读取
	// break;
	// }
	// List<String> newsTitleList = getNewsTitleOrUrl(id[i], page[j],
	// 0);
	// List<String> newsUrlList = getNewsTitleOrUrl(id[i], page[j], 1);
	// for (int k = 0; k < newsTitleList.size(); k++) {
	// List<String> newsPublishTime = getPublishTimeOrClicks(
	// newsUrlList.get(k), 0);// 发布时间
	// List<String> newsClicks = getPublishTimeOrClicks(
	// newsUrlList.get(k), 1);// 点击数
	// List<String> newsImagesUrl = getNewsImages(newsUrlList
	// .get(k));
	// NewsEntity newsEntity = NewsEntity.getInstance(
	// newsTitleList.get(k), newsUrlList.get(k),
	// newsPublishTime.get(0), newsClicks.get(0),
	// newsImagesUrl.get(0), newsImagesUrl.get(1),
	// newsImagesUrl.get(2));
	// DB.insert(newsEntity, Contants.TABLE_NAME[i]);
	// }
	// }
	// }
	// }

	/** 从网站更新全部频道新闻 */
	public static void upDateAllNews() {
		for (int j = 0; j < Contants.NEWS_URL_ID.length; j++) {
			List<String> sourceUrlFromWeb = getNewsTitleOrUrl(
					Contants.NEWS_URL_ID[j], 1, 1);
			List<String> sourceUrlFromDb = DB.query(Contants.TABLE_NAME[j]);
			for (int i = 0; i < sourceUrlFromWeb.size(); i++) {
				if (!sourceUrlFromDb.get(0).equals(sourceUrlFromWeb.get(i))) {
					String title = getNewsTitleOrUrl(Contants.NEWS_URL_ID[j],
							1, 0).get(i);
					String sourceurl = sourceUrlFromWeb.get(i);
					String publishTime = getPublishTimeOrClicks(sourceurl, 0)
							.get(0);
					String clicks = getPublishTimeOrClicks(sourceurl, 1).get(0);
					List<String> newsImagesUrl = getNewsImages(sourceurl);
					NewsEntity newsEntity = NewsEntity.getInstance(title,
							sourceurl, publishTime, clicks,
							newsImagesUrl.get(0), newsImagesUrl.get(1),
							newsImagesUrl.get(2));
					DB.insert(newsEntity, Contants.TABLE_NAME[j]);
					System.out.println(newsEntity);
				} else {
					break;
				}
			}
		}
	}

	/** 更新制定频道新闻 */
	public static List<NewsEntity> upDateNewsFromWebById(int newsUrlId) {
		List<NewsEntity> newsEntities = new ArrayList<NewsEntity>();
		List<String> sourceUrlFromWeb = getNewsTitleOrUrl(
				Contants.NEWS_URL_ID[newsUrlId], 1, 1);
		List<String> sourceUrlFromDb = DB.query(Contants.TABLE_NAME[newsUrlId]);
		for (int i = 0; i < sourceUrlFromWeb.size(); i++) {
			if (!sourceUrlFromDb.get(0).equals(sourceUrlFromWeb.get(i))) {
				String title = getNewsTitleOrUrl(
						Contants.NEWS_URL_ID[newsUrlId], 1, 0).get(i);
				String sourceurl = sourceUrlFromWeb.get(i);
				String publishTime = getPublishTimeOrClicks(sourceurl, 0)
						.get(0);
				String clicks = getPublishTimeOrClicks(sourceurl, 1).get(0);
				List<String> newsImagesUrl = getNewsImages(sourceurl);
				NewsEntity newsEntity = NewsEntity.getInstance(title,
						sourceurl, publishTime, clicks, newsImagesUrl.get(0),
						newsImagesUrl.get(1), newsImagesUrl.get(2));
				DB.insert(newsEntity, Contants.TABLE_NAME[newsUrlId]);
				newsEntities.add(newsEntity);
				System.out.println(newsEntity);
			} else {
				break;
			}
		}
		return newsEntities;
	}

	/** 处理客户端刷洗请求，从数据库中读取最新新闻并发送 */
	public static List<NewsEntity> upDateNewsFromDBById(int newsUrlId,
			String url) {
		List<NewsEntity> queryNewsEntities = new ArrayList<NewsEntity>();
		List<NewsEntity> newerNewsEntities = new ArrayList<NewsEntity>();
		queryNewsEntities = DB.queryAll(Contants.TABLE_NAME[newsUrlId]);// 以升序URL查找数据库信息
		for (int i = 0; i < queryNewsEntities.size(); i++) {
			if (queryNewsEntities.get(i).getSuorceUrl().equals(url)) {// /有最新新聞
				break;
			} else {
				newerNewsEntities.add(queryNewsEntities.get(i));
				System.out.println("更新新新闻："
						+ queryNewsEntities.get(i).getTitle());
			}
		}
		return newerNewsEntities;

	}

	public static void main(String[] args) {
		upDateAllNews();
	}
}
