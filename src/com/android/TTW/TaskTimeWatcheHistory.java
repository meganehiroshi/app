package com.android.TTW;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.android.TTW.dao.*;
import com.android.TTW.info.*;
import com.android.TTW.util.OriginalUtil;
import com.android.TTW.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TaskTimeWatcheHistory extends Activity implements OnClickListener,OnItemClickListener {


	private ListView listView;
	
	private final static  int REQUEST_CODE = 0;
	
	private List<TaskListBean>listTaskListBean;
	private ArrayAdapter<TaskListBean> arrayAdapter ;
	
	public EditText edittext;
	public int id;
	public String wheredate = null;
	public String month =null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	if(extras.getString("wheredate") != null){
        		wheredate = extras.getString("wheredate");
        	}
        }    
		
		if(wheredate == null){
		    Date date1 = new Date();  //(1)Dateオブジェクトを生成
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-01");	    //(2)SimpleDateFormatオブジェクトを生成
		    wheredate = format.format(date1);
		}
        
        setContentView(R.layout.historylist);       
       
        listView = (ListView)findViewById(R.id.list_history);
        listView.setOnItemClickListener(this);
        //listView.setLayoutParams(new LayoutParams(WC,WC));
        //layout.addView(listview);
        
        TextView backmonth = (TextView)findViewById(R.id.monthback);
        TextView nextmonth = (TextView)findViewById(R.id.monthnext);
        TextView currentmonth = (TextView)findViewById(R.id.month);

        //Calendar cal = toCalendar(wheredate);
        //date = cal.getTime();
        OriginalUtil orgutil = new OriginalUtil();
        currentmonth.setText(orgutil.makeTitleMonth(wheredate));
        //makeTaskList(orgutil.makeTitleMonth(wheredate));
        makeTaskList(wheredate);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(TaskTimeWatcheHistory.this,TaskTimeWatcheEditor.class);
		startActivityForResult(intent,REQUEST_CODE);
	}


	public void onClickMonthNext(View v) {
		
		OriginalUtil orgutil = new OriginalUtil();
		String date ;
		date = orgutil.addMonthS(wheredate, 1);
		// TODO Auto-generated method stub
		Intent intent = new Intent(TaskTimeWatcheHistory.this,TaskTimeWatcheHistory.class);
		intent.putExtra("wheredate",date);
		//startActivityForResult(intent,REQUEST_CODE);
		startActivity(intent);
	}


	public void onClickMonthBack(View v) {
		OriginalUtil orgutil = new OriginalUtil();
		String date = orgutil.addMonthS(wheredate, -1);
		// TODO Auto-generated method stub
		Intent intent = new Intent(TaskTimeWatcheHistory.this,TaskTimeWatcheHistory.class);
		intent.putExtra("wheredate",date);
		//startActivityForResult(intent,REQUEST_CODE);
		startActivity(intent);
	}
	
	@Override
	public void onItemClick(AdapterView<?> view, View list_textview, int position, long id) {

		final ArrayAdapter<TaskListBean> adapter = arrayAdapter;
		final TaskListBean taskListBean = (TaskListBean)view.getItemAtPosition(position);
		final View v = list_textview;
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
		// アラートダイアログのタイトルを設定します
	        alertDialogBuilder.setTitle("操作");
	        
	        // アラートダイアログのメッセージを設定します
	        //alertDialogBuilder.setMessage("選択したリストを・・・");
	        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_info_details);
	       
	        
	        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
	        alertDialogBuilder.setPositiveButton("編集",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {

	                		
	                		Intent intent = new Intent(TaskTimeWatcheHistory.this,TaskTimeWatcheEditor.class);
	                		intent.putExtra("createtime", taskListBean.getCreatetime());

	                		startActivityForResult(intent,REQUEST_CODE);
	                    	
	                    }
	                });
	        
	        
	        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
	        alertDialogBuilder.setNeutralButton("削除",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {

	                		TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(v.getContext());
	                		SQLiteDatabase db = dbHelper.getWritableDatabase();
	                		TaskTimeDao dao = new TaskTimeDao(db);
	                		dao.deleteTaskList(taskListBean);
	                		db.close();
	                		
	                		adapter.remove(taskListBean);
	                		adapter.notifyDataSetChanged();
	                		//finish();
	                		//startActivity(getIntent());
	                    }
	                });
	        
	        // アラートダイアログの否定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
	        alertDialogBuilder.setNegativeButton("コピー",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	
	            		    Date date1 = new Date();  //(1)Dateオブジェクトを生成
	            		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	    //(2)SimpleDateFormatオブジェクトを生成
	            		    String copytime = format.format(date1);
	            		    
	                		Intent intent = new Intent(TaskTimeWatcheHistory.this,TaskTimeWatcheEditor.class);
	                		
	                		intent.putExtra("createtime", taskListBean.getCreatetime());
	                		intent.putExtra("copytime", copytime);
	                		
	                		startActivityForResult(intent,REQUEST_CODE);
	                    
	                    }
	                });
	        // アラートダイアログのキャンセルが可能かどうかを設定します
	        alertDialogBuilder.setCancelable(true);
	        AlertDialog alertDialog = alertDialogBuilder.create();
	        // アラートダイアログを表示します
	        alertDialog.show();
	        
	}


	
public void onLongClick(AdapterView<?> view, View list_textview, int position, long id) {

		final View v = list_textview;
		final TaskListBean taskListBean = (TaskListBean)view.getItemAtPosition(position);
    	
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
		// アラートダイアログのタイトルを設定します
	        alertDialogBuilder.setTitle("削除");
	        
	        // アラートダイアログのメッセージを設定します
	        alertDialogBuilder.setMessage("削除します");
	        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_info_details);
	       
	        
	        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
	        alertDialogBuilder.setPositiveButton("実行",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {

	                		TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(v.getContext());
	                		SQLiteDatabase db = dbHelper.getWritableDatabase();
	                		TaskTimeDao dao = new TaskTimeDao(db);
	                		dao.deleteTaskList(taskListBean);
	                		db.close();
	                    	
	                    }
	                });
	        // アラートダイアログの否定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
	        alertDialogBuilder.setNegativeButton("戻る",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    		                    
	                    }
	                });
	        

	        // アラートダイアログのキャンセルが可能かどうかを設定します
	        alertDialogBuilder.setCancelable(true);
	        AlertDialog alertDialog = alertDialogBuilder.create();
	        // アラートダイアログを表示します
	        alertDialog.show();
	        
		
		
		

		
	}


	
	private void makeTaskList(String wheredate){
		
		

		
		TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		TaskTimeDao dao = new TaskTimeDao(db);
		listTaskListBean = dao.getAllTaskListByMonth(wheredate);
		db.close();
		

		//ArrayAdapter<TaskListBean> arrayAdapter = new ArrayAdapter<TaskListBean>(this, R.layout.historyitem, listTaskListBean) {
		arrayAdapter = new ArrayAdapter<TaskListBean>(this, R.layout.historyitem, listTaskListBean) {
			//super(this, textViewResourceId, objects);   
			private LayoutInflater mInflater;  
			   // 初期化子（コンストラクタの代わり）  
			   {  
			    mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);  
			   }  
			   // 表示する「一行分のView」を返すメソッド  
			   @Override  
			   public View getView(int position, View convertView, ViewGroup parent) {  
			    // 表示する一行分のViewには、android.R.layout.simple_list_item_2  
			    // （中身は TwoLineListItem） を利用する  
				TaskListBean s = getItem(position);  
			    convertView = mInflater.inflate(R.layout.historyitem, null);
			    
			    TextView view = (TextView)convertView.findViewById(R.id.history_createtime);
			    TextView view2 = (TextView)convertView.findViewById(R.id.history_updatetime);
			    
			    view.setText(String.valueOf(s.getCreatetime()));  
			    //view.setText(String.valueOf(s.getUpdatetime()));
			    
			    return convertView;  
			   }  
			  };  
		listView.setAdapter(arrayAdapter);
		
	}
	
	
	
}
