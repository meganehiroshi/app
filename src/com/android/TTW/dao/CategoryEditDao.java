package com.android.TTW.dao;

import java.util.ArrayList;
import java.util.List;

import com.android.TTW.info.CategoryInfo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CategoryEditDao {

	private static final String TABLE_NAME ="category";
	private static final String[] COLUMNS ={"id","category"};
	private SQLiteDatabase db;
	
	public CategoryEditDao(SQLiteDatabase db){
		this.db= db;
	}
	
	public long insert(CategoryInfo CategoryInfo){
		ContentValues values = new ContentValues();
		values.put("category", CategoryInfo.getCategory());
		return db.insert(TABLE_NAME,null,values);
		
	}
	public int update(CategoryInfo categoryInfo){
		ContentValues values = new ContentValues();
		values.put("category",categoryInfo.getCategory());
		String whereClause = "id = " + categoryInfo.getId();
		return db.update(TABLE_NAME,values,whereClause,null);
	}
	public int delete(int id){
		String whereClause = "id = " + id;
		return db.delete(TABLE_NAME, whereClause, null);
	}
	
	public CategoryInfo findById(int id){
		String whereClause = "id = " +id;
		Cursor cursor = db.query(TABLE_NAME, COLUMNS , whereClause,null,null,null,null);
		
		while(cursor.moveToNext()){
			CategoryInfo categoryInfo = new CategoryInfo();
			categoryInfo.setId(cursor.getInt(0));
			categoryInfo.setCategory(cursor.getString(1));

						
			return categoryInfo;
		}
		return null;
		
	}
	
	public List<CategoryInfo> getAllCategory(){
		List<CategoryInfo> list = new ArrayList<CategoryInfo>();
		
		String orderBy = "id";
		Cursor cursor = db.query(TABLE_NAME, COLUMNS, null, null, null, null, orderBy);
		
		while (cursor.moveToNext()){
			CategoryInfo categoryInfo = new CategoryInfo();
			categoryInfo.setId(cursor.getInt(0));
			categoryInfo.setCategory(cursor.getString(1));
			list.add(categoryInfo);
			
		}
		return list;
	}
}
