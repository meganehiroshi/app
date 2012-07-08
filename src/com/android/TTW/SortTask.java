package com.android.TTW;


import java.util.Comparator;

import android.util.Log;

public class SortTask implements Comparator<TaskBean> {


		@Override
		public int compare(TaskBean object1, TaskBean object2) {
			
			Log.d("object1", String.valueOf((object1).getTaskId()));
			Log.d("object2", String.valueOf((object2).getTaskId()));
			
			// TODO Auto-generated method stub
			if(((object1).getTaskId() > (object2).getTaskId()) ){
				return -1;
			}else{
				return 1;
			}
			
			
		}
}
