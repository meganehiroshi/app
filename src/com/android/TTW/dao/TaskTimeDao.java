package com.android.TTW.dao;

import java.util.ArrayList;
import java.util.List;

import com.android.TTW.TaskBean;
import com.android.TTW.TaskListBean;
import com.android.TTW.info.GraphInfo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class TaskTimeDao {

	private static final String TABLE_NAME ="tasktime";
	private static final String[] COLUMNS ={"createtime","taskid","taskname","categoryid","time"};
	
	private SQLiteDatabase db;
	
	public TaskTimeDao(SQLiteDatabase db){
		this.db= db;
	}
	
	public long insert(TaskBean taskBean){
		ContentValues values = new ContentValues();
		values.put(COLUMNS[0], taskBean.getCreateTime());
		values.put(COLUMNS[1], taskBean.getTaskId());
		values.put(COLUMNS[2], taskBean.getTaskname());
		values.put(COLUMNS[3], taskBean.getCategoryId());
		values.put(COLUMNS[4], taskBean.getTime());
		return db.insert(TABLE_NAME,null,values);
		
	}
	public int update(TaskBean taskBean){
		ContentValues values = new ContentValues();

		String whereClause = COLUMNS[0] + " = '" + taskBean.getCreateTime() + "' and " + COLUMNS[1] + " = " + taskBean.getTaskId();

		values.put(COLUMNS[2], taskBean.getTaskname());
		values.put(COLUMNS[3], taskBean.getCategoryId());
		values.put(COLUMNS[4], taskBean.getTime());

		
		return db.update(TABLE_NAME,values,whereClause,null);
	}
	public int delete(TaskBean taskBean){
		String whereClause = COLUMNS[0] + " = '" + taskBean.getCreateTime() + "' and " + COLUMNS[1] + " = " + taskBean.getTaskId();
		return db.delete(TABLE_NAME, whereClause, null);
	}
	
	public int deleteTaskList(TaskListBean taskListBean){
		String whereClause = COLUMNS[0] + " = '" + taskListBean.getCreatetime() + "'";
		return db.delete(TABLE_NAME, whereClause, null);
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


	public int getMaxTaskIdByCreatetime(String createtime){
		int id;
		
		String sql =  "select max(taskid) from tasktime where createtime = '" + createtime + "' group by createtime ";		
		
		
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToNext();
		
		id = cursor.getInt(0);
		
		return id;
	}
	
}
