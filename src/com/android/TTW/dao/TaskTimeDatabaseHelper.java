package com.android.TTW.dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.android.TTW.info.CategoryInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class TaskTimeDatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME ="ttw";
	private static final String TABLE_NAME ="tasktime";
	private static final String TABLE_NAME_CATEGORY ="category";
	private static final String VIEW_tasktime="V_tasktime";
	
	private static final int DB_VERSION =6;
	private static final String[] COLUMNS ={"createtime","taskid","taskname","categoryid","time"};
	private static final String CREATE_TASKTIME_TABLE = "create table " + TABLE_NAME +
			"(createtime TEXT ," +
			"taskid INTEGER , " +
			"taskname TEXT ," +
			"categoryid INTEGER ," +
			"time TEXT ," +
			"primary key(createtime,taskid)" +
			")";
	
	private static final String CREATE_CATEGORY_TABLE = "create table " + TABLE_NAME_CATEGORY +
	"(id integer primary key autoincrement," +
	"category text" +
	")";
	
	private static final String CREATE_VIEW_tasktime = "create view " + VIEW_tasktime +
			" as select" +
			"	 tasktime.createtime as createtime" +
			"	 ,tasktime.taskid as taskid" +
			"	 ,tasktime.taskname as taskname" +
			"	 ,tasktime.categoryid as categoryid" +
			"	 ,category.category as categoryname" +
			"	 ,tasktime.time as time" +
			" from tasktime left outer join category on tasktime.categoryid = category.id ";
			
	private static final String DROP_TASKTIME_TABLE ="drop table if exists  " + TABLE_NAME ;
	private static final String DROP_CATEGORY_TABLE ="drop table if exists  " + TABLE_NAME_CATEGORY ;
	private static final String DROP_VIEW_taskname ="drop view if exists  " + VIEW_tasktime;
	
	public TaskTimeDatabaseHelper(Context context){
		super(context,DB_NAME,null,DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TASKTIME_TABLE);
		
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strSysDate = sdf.format(calendar.getTime());
		
		ContentValues values = new ContentValues();
		values.put(COLUMNS[0], strSysDate);
		values.put(COLUMNS[1], 1);
		values.put(COLUMNS[2], "çÏã∆ÇP");
		values.put(COLUMNS[3], 1);
		values.put(COLUMNS[4], 1000);
		db.insert(TABLE_NAME,null,values);
		
		values.clear();
		values.put(COLUMNS[0], strSysDate);
		values.put(COLUMNS[1], 2);
		values.put(COLUMNS[2], "çÏã∆ÇQ");
		values.put(COLUMNS[3], 2);
		values.put(COLUMNS[4], 2000);
		db.insert(TABLE_NAME,null,values);
		
		values.clear();
		values.put(COLUMNS[0], strSysDate);
		values.put(COLUMNS[1], 3);
		values.put(COLUMNS[2], "çÏã∆ÇR");
		values.put(COLUMNS[3], 3);
		values.put(COLUMNS[4], 3000);
		db.insert(TABLE_NAME,null,values);
	
		createCategoryTable(db);
		
		db.execSQL(CREATE_VIEW_tasktime);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//db.execSQL(DROP_TASKTIME_TABLE);
		//onCreate(db);
		
		//db.rawQuery("attach database \'"+Environment.getExternalStorageDirectory() + "/category.db\'"+" as category",null);
		//db.rawQuery("create table ttw.category as select * from category.category",null);
		
		
		//createCategoryTable(db);
		
		db.execSQL(DROP_VIEW_taskname);
		db.execSQL(CREATE_VIEW_tasktime);	
	}
	
	public void createCategoryTable(SQLiteDatabase db){
		// TODO Auto-generated method stub
		db.execSQL(DROP_CATEGORY_TABLE);

		
		//SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(CREATE_CATEGORY_TABLE);
		
		
		ContentValues values = new ContentValues();
		values.put("category", "íËèÌçÏã∆");
		db.insert(TABLE_NAME_CATEGORY,null,values);
		
		values.clear();
		values.put("category", "è·äQëŒâû");
		db.insert(TABLE_NAME_CATEGORY,null,values);
		
		values.clear();
		values.put("category", "ñ‚Ç¢çáÇÌÇπ");
		db.insert(TABLE_NAME_CATEGORY,null,values);
		
		values.clear();
		values.put("category", "ÉfÅ[É^ïœçX");
		db.insert(TABLE_NAME_CATEGORY,null,values);
		
		values.clear();
		values.put("category", "ÇªÇÃëº");
		db.insert(TABLE_NAME_CATEGORY,null,values);
	}

}
