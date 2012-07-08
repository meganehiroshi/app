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
	
	//���X�g�\���܂ł̃��[�f�B���O�_�C�A���O
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
    	
        //-----[�_�C�A���O�̐ݒ�]
        dialog = new ProgressDialog(this);
        //dialog.setTitle("");
        dialog.setMessage("Now Loading...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        
        //-----[���[�f�B���O�̕`��͕ʃX���b�h�ōs��]
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
		
		//createTime�Ɍ��ݓ������Z�b�g
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		createTime = sdf.format(calendar.getTime());
		
		List<TaskBean> taskBeanList = new ArrayList<TaskBean>();
		TaskBean item;
		
		for (int i =0 ;i<3;i++){
			item = new TaskBean();
	    	item.setTaskname("��Ɠ��e"+String.valueOf(i+1));
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
    	
    	//��Ɠ��e���̔ԍ��͌��ݕ\������Ă���^�X�N���v���X�P���Z�b�g
    	item.setTaskname("��Ɠ��e"+String.valueOf(taskAdapater.getCount()+1));
    	
    	//taskid��Max�l�v���X�P���Z�b�g�i�폜�ς݂̃^�X�N���l���j
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
	        // �A���[�g�_�C�A���O�̃^�C�g����ݒ肵�܂�
	        alertDialogBuilder.setTitle("���Z�b�g");
	        
	        // �A���[�g�_�C�A���O�̃��b�Z�[�W��ݒ肵�܂�
	        alertDialogBuilder.setMessage("���ׂẴ^�X�N�̎��Ԃ����Z�b�g���܂��B");
	        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_info_details);
	       
	        
	        // �A���[�g�_�C�A���O�̍m��{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
	        alertDialogBuilder.setPositiveButton("�͂�",
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
	        
	        // �A���[�g�_�C�A���O�̔ے�{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
	        alertDialogBuilder.setNegativeButton("������",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    }
	                });
	        // �A���[�g�_�C�A���O�̃L�����Z�����\���ǂ�����ݒ肵�܂�
	        alertDialogBuilder.setCancelable(true);
	        AlertDialog alertDialog = alertDialogBuilder.create();
	        // �A���[�g�_�C�A���O��\�����܂�
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
               Thread.sleep(100);//�����ŉ摜��ǂݍ��ޏ����Ȃǂ�����

               
               
               /* 
                * UI������������ꍇ��Handler�ɏ�����n��
                * �i�_�C�A���O�\�����Ă邩�炠�܂�K�v�Ȃ��C������j
                * 
                * ���ۂɍs�������̌��ʁi���[�v�J�E���^���j�𓮓I��UI���֓n�������ꍇ
                *   Handler#sendMessage()��Bundle���Z�b�g���ēn��
                * UI�ɓ��I�ϐ��i�����j��n���K�v�������ꍇ
                *   Handler#post(Runnable r)�ł��i����͏ȗ��j
                */
               
               handler.post(new Runnable() {
                   public void run() {


		               activity.setContentView(R.layout.listlayout);
		               
		               /********* ListView(Start)**********/   
		               
		               //
		               if(createTime == null){
		            	   objects = getNewTaskList();	//�V�K
		               }else{
			               objects = getTaskList();		//�X�V      	   
		               }

		               //�R�s�[�̏ꍇ�̏���
		               if(copyTime == null){
		               }else{
		            	   for (int i = 0; i < objects.size() ;i++){
		            		   objects.get(i).setTime(0L);				//���Ԃ����Z�b�g
		            		   objects.get(i).setCreateTime(copyTime);	//�^�X�N��createTime��copyTime�ɏ㏑��
		            		   objects.get(i).setRegistered(false);		//�o�^�ς݃t���O��OFF
		            	   }
		               }	            	   
	            	   
	            			               
		               createTime = objects.get(0).getCreateTime();
		               
		               taskAdapater = new TaskAdapter(activity, R.layout.task, objects);
		               

		               listView = (ListView)findViewById(R.id.listview);
		               //ListView#setOnItemClickListener()�͑���widget�ɔ������Ă��܂��g���Ȃ�		               
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
		
		               
		               //ListView�̃t�b�^�[��Add�{�^���̒ǉ�
		               View addtask = activity.getLayoutInflater().inflate(R.layout.footer, null);
		               listView.addFooterView(addtask,null,false);
		               
		               listView.setAdapter(taskAdapater);
		               

		               /********* ListView(End)**********/		               
               
		               
                   }
               });
        

           } catch (InterruptedException e) {
               e.printStackTrace();
           }

           
           //-----[�n���h���[�Ƀ��b�Z�[�W(����)�𑗐M]
           handler.sendEmptyMessage(0);
           
           //-----[�_�C�A���O�����]
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
                Thread.sleep(100);//�����ŉ摜��ǂݍ��ޏ����Ȃǂ�����

                
                
                /* 
                 * UI������������ꍇ��Handler�ɏ�����n��
                 * �i�_�C�A���O�\�����Ă邩�炠�܂�K�v�Ȃ��C������j
                 * 
                 * ���ۂɍs�������̌��ʁi���[�v�J�E���^���j�𓮓I��UI���֓n�������ꍇ
                 *   Handler#sendMessage()��Bundle���Z�b�g���ēn��
                 * UI�ɓ��I�ϐ��i�����j��n���K�v�������ꍇ
                 *   Handler#post(Runnable r)�ł��i����͏ȗ��j
                 */
                
                handler.post(new Runnable() {
                    public void run() {


                		TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(activity);
                		SQLiteDatabase db = dbHelper.getWritableDatabase();
                		TaskTimeDao dao = new TaskTimeDao(db);
                		TaskBean taskBean ;	
                		
                		//��x�Y��createtime�̃^�X�N��S�폜
                		taskBean = taskAdapater.getItem(0);
                		TaskListBean taskListBean = new TaskListBean();
                		taskListBean.setCreatetime(taskBean.getCreateTime());
                		dao.deleteTaskList(taskListBean);

                		//Adapter�Ɏc���Ă���^�X�N��Insert
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
 
            
            //-----[�n���h���[�Ƀ��b�Z�[�W(����)�𑗐M]
            handler.sendEmptyMessage(0);
            
            //-----[�_�C�A���O�����]
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