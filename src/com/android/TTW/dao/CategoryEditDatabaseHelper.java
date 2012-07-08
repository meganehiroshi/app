package com.android.TTW.dao;

import com.android.TTW.info.CategoryInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class CategoryEditDatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME =Environment.getExternalStorageDirectory() + "/category.db";
	private static final String TABLE_NAME ="category";
	private static final int DB_VERSION =2;
	private static final String CREATE_SCHEDULE_TABLE = "create table " + TABLE_NAME +
			"(id integer primary key autoincrement," +
			"category text" +
			")";

			
	private static final String DROP_SCHEDULE_TABLE ="drop table if exists  " + TABLE_NAME ;
	
	public CategoryEditDatabaseHelper(Context context){
		super(context,DB_NAME,null,DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_SCHEDULE_TABLE);
		
		
		ContentValues values = new ContentValues();
		values.put("category", "定常作業");
		db.insert(TABLE_NAME,null,values);
		
		values.clear();
		values.put("category", "障害対応");
		db.insert(TABLE_NAME,null,values);
		
		values.clear();
		values.put("category", "問い合わせ");
		db.insert(TABLE_NAME,null,values);
		
		values.clear();
		values.put("category", "データ変更");
		db.insert(TABLE_NAME,null,values);
		
		values.clear();
		values.put("category", "その他");
		db.insert(TABLE_NAME,null,values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(DROP_SCHEDULE_TABLE);
		onCreate(db);
	}

}
