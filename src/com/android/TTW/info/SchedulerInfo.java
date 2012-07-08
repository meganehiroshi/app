package com.android.TTW.info;

public class SchedulerInfo {
	
	private int id;
	private String title;
	private String schedule;
	private int year;
	private int month;
	private int day;
	private String dayOfWeek;
	private int hour;
	private int minute;
	private int second;
	
	public int getSecond() {
		return second;
	}
	public void setSecond(int second) {
		this.second = second;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public int getHour() {
		return hour;
	}
	public void setHour(int hour) {
		this.hour = hour;
	}
	public int getMinute() {
		return minute;
	}
	public void setMinute(int minute) {
		this.minute = minute;
	}


	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}

	public String toStringDateformat(){
		return String.format("%04d-%02d-%02d",year,month,day);
	}
	
	public String toStringDate(){
		return String.format("%04dîN%02dåé%02dì˙",year,month,day);
	}
	/*
	public String toString(){
		return String.format("Åö [%04d/%02d/%02d(%s) %02d:%02d]\n     [%s] %s",year,month,day,dayOfWeek,hour,minute, title,schedule);
	}
	*/
}
