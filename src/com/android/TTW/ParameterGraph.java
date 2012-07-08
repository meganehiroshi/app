package com.android.TTW;

import java.io.Serializable;

public class ParameterGraph implements Serializable  {
	
	final public static int CHART_CIRCLE =0;
	final public static int CHART_BAR = 1;
	final public static int CHART_LINE = 2;
	final public static int GROUPBY_TASK = 1;
	final public static int GROUPBY_CATEGORY = 2;
	
	private String dateStart;
	private String dateEnd;
	private int graphType;
	private int groupbyTyep;
	private boolean showZeroTime;
	
	public String getDateStart() {
		return dateStart;
	}
	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public int getGraphType() {
		return graphType;
	}
	public void setGraphType(int graphType) {
		this.graphType = graphType;
	}
	public int getGroupbyTyep() {
		return groupbyTyep;
	}
	public void setGroupbyTyep(int groupbyTyep) {
		this.groupbyTyep = groupbyTyep;
	}
	public boolean isShowZeroTime() {
		return showZeroTime;
	}
	public void setShowZeroTime(boolean showZeroTime) {
		this.showZeroTime = showZeroTime;
	}
	public static int getChartCircle() {
		return CHART_CIRCLE;
	}
	public static int getChartBar() {
		return CHART_BAR;
	}
	public static int getChartLine() {
		return CHART_LINE;
	}
	

}
