package com.messagewall.core;

/*
 * 連線網址
 * 
 * */

public class Global {
	
	public static String serverTest = "http://myfirstphpapp-dapast1289sql.rhcloud.com/android_project/";

	//會員
	public static String URL_create_member = serverTest + "create_member.php";
	public static String URL_query_member = serverTest + "query_member.php";
	
	//文章
	public static String URL_create_article = serverTest + "create_article.php";
	public static String URL_get_all_article = serverTest + "get_all_article.php";
	public static String URL_search_article = serverTest + "search_article.php";
	
	//留言
	public static String URL_create_lmsg = serverTest + "create_lmsg.php";
	public static String URL_get_article_lmsg = serverTest + "get_article_lmsg.php";

}
