package com.android.TTW.info;

import com.android.TTW.util.OriginalUtil;

public class GraphInfo {
	
	private String taskname;
	private int categoryid;
	private long timesum;
	private String categoryname;
	private String createtime;
	
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getCategoryname() {
		return categoryname;
	}
	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public int getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(int categoryid) {
		this.categoryid = categoryid;
	}
	public long getTimesum() {
		return timesum;
	}
	public void setTimesum(long timesum) {
		this.timesum = timesum;
	}
	
	
	public String getTimesumConvString() {
		
		OriginalUtil orgutil = new OriginalUtil();
		orgutil.convMills(timesum);
		return orgutil.convMills(timesum);
	}

	public String getTimesumConvHour() {
		
		OriginalUtil orgutil = new OriginalUtil();
		orgutil.convMills(timesum);
		return orgutil.convMills(timesum);
	}

	
}
