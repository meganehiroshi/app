package com.android.TTW.util;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.android.TTW.TaskBean;
import com.android.TTW.TaskListBean;
import com.android.TTW.info.SchedulerInfo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class OriginalUtil {

	private static final String DATE_PATTERN = "yyyy-MM-dd";  
	
    private String _date2string(Date date) {  
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);  
        return sdf.format(date);  
    }  
      
    //String������^��Date���t�^�֕ϊ�  
    public Date _string2date(String value) {  
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);  
        try {  
            return sdf.parse(value);  
        } catch (ParseException e) {  
            return null;  
        }  
    }  
    
    public Date addMonthD(Date date,int addmonth){
    	Calendar cal1 = Calendar.getInstance();
    	
    	cal1.setTime(date);   
        //2100�N3��1������1�����Z���Ă��܂��B
        cal1.add(Calendar.MONTH, addmonth);
        return cal1.getTime();
        
    }
    
    
    public String addMonthS(String date,int addmonth){

        return _date2string(addMonthD(_string2date(date),addmonth));
        
    }

    public String makeTitleMonth(String date){
    	
        SimpleDateFormat titleformat = new SimpleDateFormat("yyyy�NMM��");
        Date d = _string2date(date);
	    
        return titleformat.format(d);
        
    }
	
    /*
     * �~���b�iLong�j��"HH:mm:ss"�����iString�j�ɂ��ĕԂ�
     */
    public String convMills(Long value){
		SimpleDateFormat D = new SimpleDateFormat("HH:mm:ss");
		D.setTimeZone(TimeZone.getTimeZone("GMT")); 
		return D.format(new Date(value));
    }
    
    /*
     * �~���b�iLong�j��"HH"�����iString�j�ɂ��ĕԂ�
     */
    public String convMillsToHours(Long value){
		SimpleDateFormat D = new SimpleDateFormat("HH");
		D.setTimeZone(TimeZone.getTimeZone("GMT")); 
		return D.format(new Date(value));
    }
    
    
    /*
     * "HH:mm:ss"�����iString�j�����ԒP�ʁidouble�j�ɂ��ĕԂ�
     */
    public double convMillssToHours(long value){
		String hhmmss;
    	int hours;
		int minutes;
		int seconds;
		double hoursD;
		
		
		//NumberFormat format = NumberFormat.getInstance();
		//format.setMaximumFractionDigits(1); //�����_����

		
		hhmmss = convMills(value);
		hours = Integer.parseInt(hhmmss.subSequence(0, 2).toString());
		minutes = Integer.parseInt(hhmmss.subSequence(3, 5).toString());
		seconds = Integer.parseInt(hhmmss.subSequence(6, 8).toString());
		hoursD = Math.round((double)(hours * 3600 + minutes * 60 + seconds) / (double)3600 * 10.0) / 10.0;
		return hoursD;
    }
    
    /*
     * "HH:mm:ss"�����iString�j��ScheduleInfo�ɂ��ĕԂ�
     */
    public SchedulerInfo convDateToScheduleinfo(String value){
    	SchedulerInfo scheduleinfo = new SchedulerInfo();
    	scheduleinfo.setHour(Integer.parseInt(value.substring(0, 2)));
    	scheduleinfo.setMinute(Integer.parseInt(value.substring(4, 6)));
    	scheduleinfo.setHour(Integer.parseInt(value.substring(8, 10)));
    	
    	return scheduleinfo;
    }

}
