package com.android.TTW;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.android.TTW.dao.CategoryEditDao;
import com.android.TTW.dao.CategoryEditDatabaseHelper;
import com.android.TTW.dao.TaskTimeDao;
import com.android.TTW.dao.TaskTimeDatabaseHelper;
import com.android.TTW.info.CategoryInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class TaskTimeWatcheEditor extends Activity implements OnClickListener, OnItemSelectedListener,OnItemClickListener {


	public LinearLayout layout ;
	
	private TaskAdapter taskAdapater;
	private List<TaskBean> objects;
	
	
	private ListView listView;
	private String createTime = null;
	private String copyTime = null;
	
	//リスト表示までのローディングダイアログ
	private ProgressDialog dialog;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle extras = getIntent().getExtras();
        if(extras != null){
        	if(extras.getString("createtime") != null){
        		createTime = extras.getString("createtime");
        	}
        	
        	if(extras.getString("copytime") != null){
            	copyTime = extras.getString("copytime");
        	}

        }    
        
        
        showProgressDailog(new LoadTaskItemProgress(this));
        
    }
    
    private void showProgressDailog(Runnable runnableClass){
    	
        //-----[ダイアログの設定]
        dialog = new ProgressDialog(this);
        //dialog.setTitle("");
        dialog.setMessage("Now Loading...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        
        //-----[ローディングの描画は別スレッドで行う]
        Thread thread = new Thread(runnableClass);
        thread.start();
        
        
    }



	private List<TaskBean> getTaskList(){
		TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		TaskTimeDao dao = new TaskTimeDao(db);
		List<TaskBean> taskBeanList = dao.findByCreateTime(createTime);
		db.close();
		dbHelper.close();
		
		return taskBeanList;
		
	} 

	private List<TaskBean> getNewTaskList(){
		
		/*
		TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		TaskTimeDao dao = new TaskTimeDao(db);
		*/
		
		//createTimeに現在日時をセット
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		createTime = sdf.format(calendar.getTime());
		
		List<TaskBean> taskBeanList = new ArrayList<TaskBean>();
		TaskBean item;
		
		for (int i =0 ;i<3;i++){
			item = new TaskBean();
	    	item.setTaskname("作業内容"+String.valueOf(i+1));
	    	//item.setTmrIsStart(false);
	       	item.setTaskId(i+1);
	       	item.setCreateTime(createTime);
	       	item.setRegistered(false);
	       	//dao.insert(item);
	       	
	       	taskBeanList.add(item);
		}
	   	
		
		
		//db.close();
		//dbHelper.close();
		
		return taskBeanList;
	} 
    
    public void onClickAddTask(View view){
    	
    	TaskBean item = new TaskBean();
    	
    	//作業内容名の番号は現在表示されているタスク数プラス１をセット
    	item.setTaskname("作業内容"+String.valueOf(taskAdapater.getCount()+1));
    	
    	//taskidはMax値プラス１をセット（削除済みのタスクを考慮）
       	item.setTaskId(getMaxTaskId(createTime)+1);
       	
       	item.setCreateTime(createTime);
       	item.setRegistered(false);
    	
    	//taskAdapater.clear();
    	taskAdapater.add(item);
    	

    	
        //Toast.makeText(this, String.valueOf(taskAdapater.getCount()),  Toast.LENGTH_SHORT).show();
    }
    
    public int getMaxTaskId(String createtime){
    	int id;
    	
    	
		TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		TaskTimeDao dao = new TaskTimeDao(db);
    	
    	id = dao.getMaxTaskIdByCreatetime(createtime);
    		
		db.close();
		dbHelper.close();
		
    	return id;
    }

    public void onClickTimeReset(View view){
    	
    	
	       AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
	        // アラートダイアログのタイトルを設定します
	        alertDialogBuilder.setTitle("リセット");
	        
	        // アラートダイアログのメッセージを設定します
	        alertDialogBuilder.setMessage("すべてのタスクの時間をリセットします。");
	        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_info_details);
	       
	        
	        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
	        alertDialogBuilder.setPositiveButton("はい",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	
	                    	resetTimeAll();
	                    	
	                    	/*
	                    	TaskBean taskBean;
	                    	for (int i = 0; i < taskAdapater.getCount(); i++){
	                    		taskBean = taskAdapater.getItem(i);
	                    		taskBean.setTime(0L);
	                    		taskAdapater.notifyDataSetChanged();
	                    	}
	                    	*/
	                    }
	                });
	        
	        // アラートダイアログの否定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
	        alertDialogBuilder.setNegativeButton("いいえ",
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
    	
    	
        //Toast.makeText(this, String.valueOf(taskAdapater.getCount()),  Toast.LENGTH_SHORT).show();
    }
    
    public void resetTimeAll(){
    	TaskBean taskBean;
    	for (int i = 0; i < taskAdapater.getCount(); i++){
    		taskBean = taskAdapater.getItem(i);
    		taskBean.setTime(0L);
    		taskAdapater.notifyDataSetChanged();
    	}    	
    }
    
    public void onClickSave(View view){
    	
    	
    	showProgressDailog(new SaveProgress(this));
    	
        //Toast.makeText(this, String.valueOf(taskAdapater.getCount()),  Toast.LENGTH_SHORT).show();
    }
    
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	
	

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	public void onResume(View v) {
		// TODO Auto-generated method stub

		Toast.makeText(this, "onResume",  Toast.LENGTH_SHORT).show();
	}

    private class LoadTaskItemProgress implements Runnable {
   	 
   	 Activity activity;
   	public LoadTaskItemProgress(Activity activity){
   		this.activity = activity;
   	}
   	
       public void run() {
           try {
               Thread.sleep(100);//ここで画像を読み込む処理などを書く

               
               
               /* 
                * UIを書き換える場合はHandlerに処理を渡す
                * （ダイアログ表示してるからあまり必要ない気もする）
                * 
                * 実際に行う処理の結果（ループカウンタ等）を動的にUI側へ渡したい場合
                *   Handler#sendMessage()にBundleをセットして渡す
                * UIに動的変数（引数）を渡す必要が無い場合
                *   Handler#post(Runnable r)でも可（今回は省略）
                */
               
               handler.post(new Runnable() {
                   public void run() {


		               activity.setContentView(R.layout.listlayout);
		               
		               /********* ListView(Start)**********/   
		               
		               //
		               if(createTime == null){
		            	   objects = getNewTaskList();	//新規
		               }else{
			               objects = getTaskList();		//更新      	   
		               }

		               //コピーの場合の処理
		               if(copyTime == null){
		               }else{
		            	   for (int i = 0; i < objects.size() ;i++){
		            		   objects.get(i).setTime(0L);				//時間をリセット
		            		   objects.get(i).setCreateTime(copyTime);	//タスクのcreateTimeをcopyTimeに上書き
		            		   objects.get(i).setRegistered(false);		//登録済みフラグをOFF
		            	   }
		               }	            	   
	            	   
	            			               
		               createTime = objects.get(0).getCreateTime();
		               
		               taskAdapater = new TaskAdapter(activity, R.layout.task, objects);
		               

		               listView = (ListView)findViewById(R.id.listview);
		               //ListView#setOnItemClickListener()は他のwidgetに反応してしまい使えない		               
		               listView.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							// TODO Auto-generated method stub
							
							Toast.makeText(activity, "onResume",  Toast.LENGTH_SHORT).show();
							return false;
						}
		            	   
					});
		               
		               TextView textview = (TextView)findViewById(R.id.TextView_title);
		               textview.setText(createTime);
		
		               
		               //ListViewのフッターにAddボタンの追加
		               View addtask = activity.getLayoutInflater().inflate(R.layout.footer, null);
		               listView.addFooterView(addtask,null,false);
		               
		               listView.setAdapter(taskAdapater);
		               

		               /********* ListView(End)**********/		               
               
		               
                   }
               });
        

           } catch (InterruptedException e) {
               e.printStackTrace();
           }

           
           //-----[ハンドラーにメッセージ(引数)を送信]
           handler.sendEmptyMessage(0);
           
           //-----[ダイアログを閉じる]
           dialog.dismiss();
       }
   }
	
    private class SaveProgress implements Runnable {
    	 
    	 Activity activity;
    	public SaveProgress(Activity activity){
    		this.activity = activity;
    	}
    	
        public void run() {
            try {
                Thread.sleep(100);//ここで画像を読み込む処理などを書く

                
                
                /* 
                 * UIを書き換える場合はHandlerに処理を渡す
                 * （ダイアログ表示してるからあまり必要ない気もする）
                 * 
                 * 実際に行う処理の結果（ループカウンタ等）を動的にUI側へ渡したい場合
                 *   Handler#sendMessage()にBundleをセットして渡す
                 * UIに動的変数（引数）を渡す必要が無い場合
                 *   Handler#post(Runnable r)でも可（今回は省略）
                 */
                
                handler.post(new Runnable() {
                    public void run() {


                		TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(activity);
                		SQLiteDatabase db = dbHelper.getWritableDatabase();
                		TaskTimeDao dao = new TaskTimeDao(db);
                		TaskBean taskBean ;	
                		
                		//一度該当createtimeのタスクを全削除
                		taskBean = taskAdapater.getItem(0);
                		TaskListBean taskListBean = new TaskListBean();
                		taskListBean.setCreatetime(taskBean.getCreateTime());
                		dao.deleteTaskList(taskListBean);

                		//Adapterに残っているタスクをInsert
                    	for (int i =0; i < taskAdapater.getCount() ;i++){
                    		taskBean = taskAdapater.getItem(i);
                    		
                    		/*
                    		if(taskBean.isDeleted()){
                       			dao.delete(taskBean);
                           	}else if(taskBean.isRegistered()){
                           		dao.update(taskBean);
                       		}else{
                       			dao.insert(taskBean);
                           		taskBean.setRegistered(true);	                           		
                           	}
                    		*/
                   			dao.insert(taskBean);
                       		taskBean.setRegistered(true);
                    	}
                    		
                		db.close();
                		dbHelper.close();
                
                
                    }
                });
         

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
 
            
            //-----[ハンドラーにメッセージ(引数)を送信]
            handler.sendEmptyMessage(0);
            
            //-----[ダイアログを閉じる]
            dialog.dismiss();
        }
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "onResume",  Toast.LENGTH_SHORT).show();
		
	}
	
}