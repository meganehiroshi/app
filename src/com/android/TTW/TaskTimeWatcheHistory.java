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
		    Date date1 = new Date();  //(1)Date�I�u�W�F�N�g�𐶐�
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-01");	    //(2)SimpleDateFormat�I�u�W�F�N�g�𐶐�
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
		// �A���[�g�_�C�A���O�̃^�C�g����ݒ肵�܂�
	        alertDialogBuilder.setTitle("����");
	        
	        // �A���[�g�_�C�A���O�̃��b�Z�[�W��ݒ肵�܂�
	        //alertDialogBuilder.setMessage("�I���������X�g���E�E�E");
	        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_info_details);
	       
	        
	        // �A���[�g�_�C�A���O�̍m��{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
	        alertDialogBuilder.setPositiveButton("�ҏW",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {

	                		
	                		Intent intent = new Intent(TaskTimeWatcheHistory.this,TaskTimeWatcheEditor.class);
	                		intent.putExtra("createtime", taskListBean.getCreatetime());

	                		startActivityForResult(intent,REQUEST_CODE);
	                    	
	                    }
	                });
	        
	        
	        // �A���[�g�_�C�A���O�̍m��{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
	        alertDialogBuilder.setNeutralButton("�폜",
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
	        
	        // �A���[�g�_�C�A���O�̔ے�{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
	        alertDialogBuilder.setNegativeButton("�R�s�[",
	                new DialogInterface.OnClickListener() {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which) {
	                    	
	            		    Date date1 = new Date();  //(1)Date�I�u�W�F�N�g�𐶐�
	            		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	    //(2)SimpleDateFormat�I�u�W�F�N�g�𐶐�
	            		    String copytime = format.format(date1);
	            		    
	                		Intent intent = new Intent(TaskTimeWatcheHistory.this,TaskTimeWatcheEditor.class);
	                		
	                		intent.putExtra("createtime", taskListBean.getCreatetime());
	                		intent.putExtra("copytime", copytime);
	                		
	                		startActivityForResult(intent,REQUEST_CODE);
	                    
	                    }
	                });
	        // �A���[�g�_�C�A���O�̃L�����Z�����\���ǂ�����ݒ肵�܂�
	        alertDialogBuilder.setCancelable(true);
	        AlertDialog alertDialog = alertDialogBuilder.create();
	        // �A���[�g�_�C�A���O��\�����܂�
	        alertDialog.show();
	        
	}


	
public void onLongClick(AdapterView<?> view, View list_textview, int position, long id) {

		final View v = list_textview;
		final TaskListBean taskListBean = (TaskListBean)view.getItemAtPosition(position);
    	
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
		// �A���[�g�_�C�A���O�̃^�C�g����ݒ肵�܂�
	        alertDialogBuilder.setTitle("�폜");
	        
	        // �A���[�g�_�C�A���O�̃��b�Z�[�W��ݒ肵�܂�
	        alertDialogBuilder.setMessage("�폜���܂�");
	        alertDialogBuilder.setIcon(android.R.drawable.ic_menu_info_details);
	       
	        
	        // �A���[�g�_�C�A���O�̍m��{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
	        alertDialogBuilder.setPositiveButton("���s",
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
	        // �A���[�g�_�C�A���O�̔ے�{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
	        alertDialogBuilder.setNegativeButton("�߂�",
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
			   // �������q�i�R���X�g���N�^�̑���j  
			   {  
			    mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);  
			   }  
			   // �\������u��s����View�v��Ԃ����\�b�h  
			   @Override  
			   public View getView(int position, View convertView, ViewGroup parent) {  
			    // �\�������s����View�ɂ́Aandroid.R.layout.simple_list_item_2  
			    // �i���g�� TwoLineListItem�j �𗘗p����  
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
