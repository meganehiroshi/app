package com.android.TTW;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.Chronometer;

public class MyChronometer extends Chronometer {


	
	public MyChronometer(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

	}
	private long startTime;
	private long stopTime;
	private boolean tmrIsStart;
	
	public boolean isTmrIsStart() {
		return tmrIsStart;
	}
	public void setTmrIsStart(boolean tmrIsStart) {
		this.tmrIsStart = tmrIsStart;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getStopTime() {
		return stopTime;
	}
	public void setStopTime(long stopTime) {
		this.stopTime = stopTime;
	}
	
	public void setMyBase(){
		
		if(tmrIsStart){
			setBase(SystemClock.elapsedRealtime()- (stopTime - startTime));	
		}else{
			setBase(SystemClock.elapsedRealtime());	
		}
		
	}
	
	
	
}
