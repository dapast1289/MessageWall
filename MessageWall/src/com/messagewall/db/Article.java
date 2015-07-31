package com.messagewall.db;

public class Article {

	private int pid;
	private String title;
	private String author;
	private int popularity;
	private String content;

	public Article() {
		this.pid = 0;
		this.title = "";
		this.author = "";
		this.popularity = 0;
		this.content = "";
	}
	
	public Article(int pid, String title, String author, int popularity,
			String content) {
		this.pid = pid;
		this.title = title;
		this.author = author;
		this.popularity = popularity;
		this.content = content;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
