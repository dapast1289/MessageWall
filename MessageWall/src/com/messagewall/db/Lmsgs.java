package com.messagewall.db;

public class Lmsgs {

	private int article_pid;
	private int pid;
	private String author;
	private String msg;

	public Lmsgs(int article_pid, int pid, String author, String msg) {
		this.article_pid = article_pid;
		this.pid = pid;
		this.author = author;
		this.msg = msg;
	}

	public int getArticle_pid() {
		return article_pid;
	}

	public void setArticle_pid(int article_pid) {
		this.article_pid = article_pid;
	}

	public int getPid() {
		return pid;
	}
	
	public void setPid(int pid) {
		this.pid = pid;
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
