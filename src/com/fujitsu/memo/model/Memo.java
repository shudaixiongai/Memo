package com.fujitsu.memo.model;

public class Memo {

	private int id;
	private String time;
	private String title;
	private int type;
	private int collection;
	private String content;
	private String remindtime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCollection() {
		return collection;
	}
	public void setCollection(int collection) {
		this.collection = collection;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRemindtime() {
		return remindtime;
	}
	public void setRemindtime(String remindtime) {
		this.remindtime = remindtime;
	}
}
