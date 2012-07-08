package com.android.TTW;

import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Spinner;

public class TaskBean {

	private String createTime ;

	private int taskid;
	private String taskname;
	private int categoryid=0;
	private Long startTime = 0L;
	private Long stopTime = 0L;
	private boolean registered = true;//DatabaseÇÃTBLÇ…ÇÕñ≥Ç¢çÄñ⁄
	private boolean deleted = false;
	//private boolean tmrIsStart=true;



	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public boolean isRegistered() {
		return registered;
	}
	public void setRegistered(boolean registered) {
		this.registered = registered;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}	
	public int getCategoryId() {
		return categoryid;
	}
	public void setCategoryId(int categoryid) {
		this.categoryid = categoryid;
	}
	
	public Long getStopTime() {
		return stopTime;
	}
	public void setStopTime(Long stopTime) {
		this.stopTime = stopTime;
	}
	
	/*
	public boolean getTmrIsStart() {
		return tmrIsStart;
	}
	public void setTmrIsStart(boolean tmrIsStart) {
		this.tmrIsStart = tmrIsStart;
	}
*/
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	

	
	
	public int getTaskId() {
		return taskid;
	}
	public void setTaskId(int taskid) {
		this.taskid = taskid;
	}
	
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	
	
	public Long getTime() {
		return this.stopTime - this.startTime;
	}
	
	public void setTime(Long time) {
		this.stopTime= time;
		this.startTime = 0L;
	}
}
