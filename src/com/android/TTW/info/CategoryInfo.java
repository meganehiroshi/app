package com.android.TTW.info;

public class CategoryInfo {
	
	private int id;
	private String category;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	

	@Override
	public String toString(){
		//return String.format("Åö [%04d/%02d/%02d(%s) %02d:%02d]\n     [%s] %s",category);
		return category;
	}
}
