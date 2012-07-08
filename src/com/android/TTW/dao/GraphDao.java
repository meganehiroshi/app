package com.android.TTW.dao;

import java.util.ArrayList;
import java.util.List;

import com.android.TTW.ParameterGraph;
import com.android.TTW.TaskBean;
import com.android.TTW.TaskListBean;
import com.android.TTW.info.GraphInfo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GraphDao {

	private static final String TABLE_NAME ="tasktime";
	private static final String VIEW_NAME ="V_tasktime";
	
	private static final String[] COLUMNS ={"createtime","taskid","taskname","categoryid","time"};
	private static final String[] COL_GROUP_TASK_CATEGORY ={"taskname","categoryid","sum(time) as timesum"};
	private static final String[] COL_GROUP_TASK_CATEGORY_DATE ={"taskname","categoryid","sum(time) as timesum","createtime"};
	private static final String[] COL_GROUP_CATEGORY ={"categoryname","sum(time) as timesum"};
	private static final String[] COL_GROUP_CATEGORY_DATE ={"categoryname","categoryid","sum(time) as timesum","createtime"};
	private static final String[] COL_TOTALTIME ={"sum(time) as timesum"};
	private static final String[] COL_TOTALTIME_CREATETIME ={"sum(time) as timesum ,createtime"};
	
	private static final String GRPBY_TASK_CATEGORY = "taskname"; 
	private static final String GRPBY_CREATETIME = "createtime"; 
	private static final String GRPBY_CATEGORY = "categoryname"; 
	private static final String GRPBY_CATEGORY_DATE = "categoryname,categoryid,createtime";
	
	private static final String HAVING_OMIT_TIME_ZERO = "timesum > 0";
	
	private SQLiteDatabase db;
	
	public GraphDao(SQLiteDatabase db){
		this.db= db;
	}
	
	//保存リスト表示用
	public List<TaskListBean> getAllTaskListByMonth(String yearmonth){
		
		//パラメタの日付と同じ月のタスクの一覧を取得する
		String[] COL = {COLUMNS[0]};
		
		//DBのcreatetime、パラメタ日付ともにdatetime()関数で月初にして比較
		String whereClause =  "datetime(createtime, 'start of month') = datetime('" + yearmonth + "', 'start of month')" ;
		//				" and createtime < datetime(datetime('" + yearmonth + "', '+1 months'), 'start of month')";
		Cursor cursor = db.query(TABLE_NAME, COL , whereClause,null,"createtime",null,null);
		
		List<TaskListBean> tasktimeList = new ArrayList<TaskListBean>();
		
		while(cursor.moveToNext()){
			TaskListBean taskListBean = new TaskListBean();
			
			taskListBean.setCreatetime(cursor.getString(0));
			
			tasktimeList.add(taskListBean);
			//taskListBean.setUpdatetime(cursor.getString(1));
		}
		return tasktimeList;
		
	}
	
	public List<TaskBean> findByCreateTime(String createtime ){
		
		String whereClause =  "createtime = '" + createtime + "'";		
		List<TaskBean> list = new ArrayList<TaskBean>();
		
		String orderBy = COLUMNS[0] + " , " + COLUMNS[1]  ;
		Cursor cursor = db.query(TABLE_NAME, COLUMNS , whereClause,null,null,null,orderBy);
		
		while(cursor.moveToNext()){
			TaskBean taskBean = new TaskBean();
			taskBean.setCreateTime(cursor.getString(0));
			taskBean.setTaskId(cursor.getInt(1));
			taskBean.setTaskname(cursor.getString(2));
			taskBean.setCategoryId(cursor.getInt(3));
			taskBean.setTime(Long.valueOf(cursor.getString(4)));
						
			list.add(taskBean);
		}
		return list;
		
	}
	
	public List<TaskBean> findAll(){
		
		List<TaskBean> list = new ArrayList<TaskBean>();
		
		String orderBy = COLUMNS[0] + " , " + COLUMNS[1]  ;
		Cursor cursor = db.query(TABLE_NAME, COLUMNS, null, null, null, null, orderBy);
		
		while (cursor.moveToNext()){
			TaskBean taskBean = new TaskBean();
			taskBean.setCreateTime(cursor.getString(0));
			taskBean.setTaskId(cursor.getInt(1));
			taskBean.setTaskname(cursor.getString(2));
			taskBean.setCategoryId(cursor.getInt(3));
			taskBean.setTime(Long.valueOf(cursor.getString(4)));
			
			list.add(taskBean);
			
		}
		return list;
	}

	public List<GraphInfo> getGraphDataCircle(ParameterGraph param){
		
		String whereClause =  "createtime between '" + param.getDateStart() + "' and '" + param.getDateEnd() + "'";
		
		List<GraphInfo> list = new ArrayList<GraphInfo>();
		String orderBy = "timesum desc" ;
		String having ="";
		
		if(param.isShowZeroTime()){
		}else{
			having = HAVING_OMIT_TIME_ZERO;
		}
		
		Cursor cursor;
		
		if (param.getGroupbyTyep() == ParameterGraph.GROUPBY_TASK){
			cursor = db.query(VIEW_NAME, COL_GROUP_TASK_CATEGORY , whereClause,null,GRPBY_TASK_CATEGORY,having,orderBy);
			while(cursor.moveToNext()){
				GraphInfo graphInfo = new GraphInfo();
				graphInfo.setTaskname(cursor.getString(0));
				graphInfo.setCategoryid(cursor.getInt(1));
				graphInfo.setTimesum(Long.valueOf(cursor.getString(2)));
							
				list.add(graphInfo);
			}
		}else if (param.getGroupbyTyep() == ParameterGraph.GROUPBY_CATEGORY){
			cursor = db.query(VIEW_NAME, COL_GROUP_CATEGORY , whereClause,null,GRPBY_CATEGORY,having,orderBy);
			while(cursor.moveToNext()){
				GraphInfo graphInfo = new GraphInfo();
				graphInfo.setCategoryname(cursor.getString(0));
				graphInfo.setTimesum(Long.valueOf(cursor.getString(1)));
							
				list.add(graphInfo);
			}
		}

		return list;
		
	}
	
	public long getTotalTime(ParameterGraph param ){
		String dateStart = param.getDateStart() + " 00:00:00";
		String dateEnd = param.getDateEnd() + " 24:00:00";
		
		String whereClause =  "createtime between '" + dateStart + "' and '" + dateEnd + "'";
		Cursor cursor;

		cursor = db.query(TABLE_NAME, COL_TOTALTIME , whereClause,null,null,null,null);
		cursor.moveToNext();
		long time = Long.valueOf(cursor.getString(0));
		
		return time;
		
	}
	
	public List<GraphInfo> getGraphDataBar(ParameterGraph param){
		
		String whereClause =  "createtime between '" + param.getDateStart() + "' and '" + param.getDateEnd() + "'";
		
		List<GraphInfo> list = new ArrayList<GraphInfo>();
		String orderBy = "createtime" ;
		String having ="";
		
		if(param.isShowZeroTime()){
		}else{
			having = HAVING_OMIT_TIME_ZERO;
		}
		
		Cursor cursor;
		cursor = db.query(VIEW_NAME, COL_TOTALTIME_CREATETIME , whereClause,null,GRPBY_CREATETIME,having,orderBy);
		
		while(cursor.moveToNext()){
			GraphInfo graphInfo = new GraphInfo();
			graphInfo.setTimesum(Long.valueOf(cursor.getString(0)));
			graphInfo.setCreatetime(cursor.getString(1));
			
			list.add(graphInfo);		
		}

		return list;
		
	}

	
	public List<GraphInfo> getGraphDataBarStacke(ParameterGraph param){
		
		String whereClause =  "createtime between '" + param.getDateStart() + "' and '" + param.getDateEnd() + "'";
		
		List<GraphInfo> list = new ArrayList<GraphInfo>();
		String orderBy = "createtime,categoryid" ;
		String having ="";
		
		if(param.isShowZeroTime()){
		}else{
			having = HAVING_OMIT_TIME_ZERO;
		}
		
		Cursor cursor;
		

		cursor = db.query(VIEW_NAME, COL_GROUP_CATEGORY_DATE , whereClause,null,GRPBY_CATEGORY_DATE,having,orderBy);
		while(cursor.moveToNext()){
			GraphInfo graphInfo = new GraphInfo();
			graphInfo.setCategoryname(cursor.getString(0));
			graphInfo.setCategoryid(cursor.getInt(1));
			graphInfo.setTimesum(Long.valueOf(cursor.getString(2)));
			graphInfo.setCreatetime(cursor.getString(3));			
			
			list.add(graphInfo);
		}

		return list;
		
	}
	
	
}
