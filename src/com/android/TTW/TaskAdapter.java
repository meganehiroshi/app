package com.android.TTW;

import java.util.List;

import com.android.TTW.dao.CategoryEditDao;
import com.android.TTW.dao.TaskTimeDatabaseHelper;
import com.android.TTW.info.CategoryInfo;
import com.android.TTW.info.SchedulerInfo;
import com.android.TTW.util.OriginalUtil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;

public class TaskAdapter extends ArrayAdapter<TaskBean>  {
	 private LayoutInflater layoutInflater_;
	 private List<CategoryInfo> TaskList;
	 
	 private String[] categorylist ;
	 public EditText edittext;
	 
	 public TaskAdapter(Context context, int textViewResourceId, List<TaskBean> objects) {
		 super(context, textViewResourceId, objects);
		 layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 
		 //�J�e�S���[�̎擾
		 createCategoryList();
	 }
	 
	 /* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 
		 ViewHolder holder; 
		 
		 // ����̍s(position)�̃f�[�^�𓾂�
		 final TaskBean item = getItem(position);
		 
		 //item.setSortid(position);
		 //Log.d("[debug]", "position : "+String.valueOf(position));
		 //Log.d("[debug]", "parent_ChildCount : "+String.valueOf(hoge));
		 //Log.d("[debug]", "parent_Id : "+parent.getId());
		 //Log.d("[debug]", "getItemViewType : "+String.valueOf(getItemViewType(position)));
		 //Log.d("[debug]", "parent"+parent.get);
		 
		 // convertView�͎g���񂵂���Ă���\��������̂�null�̎������V�������
		 if (null == convertView) {

			convertView = layoutInflater_.inflate(R.layout.task, null);
			
			// TaskBean�̃f�[�^��View�̊eWidget�ɃZ�b�g����
			MyChronometer chrono= (MyChronometer)convertView.findViewById(R.id.chronometer_id);

			
			
			//EditText textView = (EditText)convertView.findViewById(R.id.EditText01);
			TextView textView2 = (TextView)convertView.findViewById(R.id.TextView_Taskname);
			//Log.d("[debug]if", "textview : "+String.valueOf(textView.getText().toString())); 
			
			Button  button = (Button)convertView.findViewById(R.id.Button01);
			
			//Spinner spinner = (Spinner) convertView.findViewById(R.id.Spinner01);
			TextView textViewCaterogy = (TextView)convertView.findViewById(R.id.TextView_Category);
			
			holder = new ViewHolder();
			//holder.taskname =  textView;
			holder.taskname2 = textView2;
			holder.chronometer = chrono;
			holder.button = button;
			//holder.spinner = spinner;
			holder.category = textViewCaterogy;
			
			
			holder.sortid = position; 
			holder.tmrIsStart =false;
			holder.startTime = SystemClock.elapsedRealtime();
			holder.stopTime = 0L;
			
			 convertView.setTag(holder);
			 
		 }else{
			 holder = (ViewHolder)convertView.getTag();
			 //Log.d("[debug]else", "convertV Class : "+ convertView.getClass().toString());
		 }

		 holder.taskname2.setText(item.getTaskname());
		 holder.taskname2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//�N���b�N���ꂽ�^�X�N����TextView���擾
				final TextView textview_taskname = (TextView)v.findViewById(R.id.TextView_Taskname);
				
				//�N���b�N���ꂽ�^�X�N���̉��ɂ���J�e�S���[��TextView���擾
				LinearLayout parent1 = (LinearLayout)v.getParent();
				LinearLayout parent2 = (LinearLayout)parent1.getParent();
				LinearLayout parent3 = (LinearLayout)parent2.getParent();
				final TextView textview_category = (TextView)parent3.findViewById(R.id.TextView_Category);
				
				// �A���[�g�_�C�A���O�̐ݒ�
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
				alertDialogBuilder.setTitle("�ҏW");
				//alertDialogBuilder.setMessage(textview.getText().toString());			        
				
				// �A���[�g�_�C�A���O�ɓƎ�layout�t�@�C����ݒu
				LayoutInflater inflater = LayoutInflater.from(v.getContext());
				final View taskEdit = inflater.inflate(R.layout.task_edit, null);
				alertDialogBuilder.setView(taskEdit);
				
				
				// �A���[�g�_�C�A���O����widget�ނ̐ݒ�				
				// EditText�̐ݒ�
				final EditText edittext = (EditText)taskEdit.findViewById(R.id.EditText_Taskname);
				edittext.setText(textview_taskname.getText());
				
				// �A���[�g�_�C�A���O����widget�ނ̐ݒ�								
				// Spinner
				final Spinner spinner = (Spinner)taskEdit.findViewById(R.id.Spinner_category);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item);
				 	
			 	int selectedId =0;
			 	for (int i=0;i < categorylist.length ;i++){
			 		adapter.add(categorylist[i]);
			 		if(categorylist[i].equals(textview_category.getText().toString())){
			 			selectedId = i;		
			 		}
			 	}
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	
				adapter.notifyDataSetChanged();				
				
				spinner.setAdapter(adapter);
				spinner.setSelection(selectedId);
				spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		            @Override
		            public void onItemSelected(AdapterView<?> adapterView, View view,int position, long id) {
		            }

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {						
					}
				});
				
		        // �A���[�g�_�C�A���O�̍m��{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
		        alertDialogBuilder.setPositiveButton("�X�V",
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    	
		                    	String taskname = edittext.getText().toString();
		                    	String categoryname = spinner.getSelectedItem().toString();
		                    	item.setTaskname(taskname);
		                    	item.setCategoryId(getIdByCategory(categoryname));
		                    	
		                    	textview_taskname.setText(taskname);
		                    	textview_category.setText(categoryname);
		                    	
		                    	//finish();
		                		//startActivity(getIntent());
		                    }
		                });
		        // �A���[�g�_�C�A���O�̃j���[�g�����{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
		        alertDialogBuilder.setNeutralButton("�폜",
		                new DialogInterface.OnClickListener() {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which) {
		                    	item.setDeleted(true);
		                    	remove(item);
		                    	notifyDataSetChanged();
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
  
		 });
		 
		 holder.category.setText(getCategoryById(item.getCategoryId()));
		
		 holder.chronometer.setStartTime(item.getStartTime());
		 holder.chronometer.setStopTime(item.getStopTime());
		 //holder.chronometer.setTmrIsStart(item.getTmrIsStart());		 

		 
		 
		 //Adapter�̍ĕ`����Chronometer�̕\�������l�Z�b�g
		 holder.chronometer.setBase(SystemClock.elapsedRealtime()- (item.getStopTime() - item.getStartTime()));			 
		 
		 CharSequence text = holder.chronometer.getText();
         if (text.length()  == 5) {
        	 holder.chronometer.setText("00:"+text);
         } else if (text.length() == 7) {
        	 holder.chronometer.setText("0"+text);
         }
		 //holder.chronometer.setText("00:00:00");
		 holder.chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {

		        @Override
		        public void onChronometerTick(Chronometer chronometer) {
		            CharSequence text = chronometer.getText();
		            if (text.length()  == 5) {
		                chronometer.setText("00:"+text);
		            } else if (text.length() == 7) {
		                chronometer.setText("0"+text);
		            }
		        }
		    });
			 
		 /*
		 holder.chronometer.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					
					View root = view.getRootView();
					OriginalUtil orgUtil = new OriginalUtil();
					
					final Chronometer chrono = (Chronometer) root.findViewById(view.getId());
					final SchedulerInfo schedulerInfo = orgUtil.convDateToScheduleinfo(chrono.getText().toString());
					final TimePickerDialog timePickerDialog;
					
					OnTimeSetListener timeListener  = new OnTimeSetListener(){
	
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							// TODO Auto-generated method stub
							schedulerInfo.setHour(hour);
							schedulerInfo.setMinute(month+1);
							schedulerInfo.setSecond(day);
							
							//btnDate.setText(datestring);
							
							onClick(view);
						}
					};
						
					timePickerDialog = new TimePickerDialog(view.getContext()
							, timeListener
							, schedulerInfo.getHour()
							, schedulerInfo.getMinute()
							, false);
					//���t�_�C�A���O��\��
					timePickerDialog.show();
				}

		 });
		 */
					
		 holder.button.setOnClickListener(new OnClickListener(){
	     		private long stopTime;
	     		private long startTime=0;
	     		//private boolean isTmrStart = false;
	     		private boolean hoge = false;
	    		
	    		
	             public void onClick(View v){
	            	 
	            	 
	            	 
	            	 LinearLayout vwParentRow = (LinearLayout)v.getParent();
	            	 LinearLayout ParentTop = (LinearLayout) vwParentRow.getParent();
	            	 
	            	 Button btn = (Button)v.findViewById(R.id.Button01);
	            	 MyChronometer chrn = (MyChronometer)ParentTop.findViewById(R.id.chronometer_id);
	            	 
	            	 View rootView = (View) vwParentRow.getRootView();	            	 
	            	 ListView listview = (ListView)rootView.findViewById(R.id.listview);
	            	 
	            	 
	            	 ///////////�@���̎��s���^�C�}�[��Stop�����鏈��(S)�@///////////
	            	 if (listview != null){
		            	 
		            	 for (int i = 0 ; i < listview.getChildCount() ; i++){
		            		 MyChronometer otherChrn = (MyChronometer)listview.getChildAt(i).findViewById(R.id.chronometer_id);
		            		 Button otherBtn = (Button)listview.getChildAt(i).findViewById(R.id.Button01);
		            		 
		            		 //Chronometer���擾�ł�����
		            		 if (otherChrn != null){ 
		            			 //�����ȊO��Chronometer�ł��邱�Ƃ��`�F�b�N
		            			 if(chrn.equals(otherChrn) == false){
		            				 //Start������������s���ł���ꍇ�i���{�^���̕\����"Stop"�j
				            		 if (otherBtn.getText().toString().equals("Stop")){			
				            			//�{�^�����N���b�N��Stop������
				            		 	otherBtn.performClick();
				            		 	break;
				            		 }
			            		 }
		            		 }
		            	 }	            	 
	            	 }
	            	 ///////////�@���̃^�C�}�[��Stop�����鏈��(E)�@///////////

           	            	

	             	if(btn.getText().equals("Start")){
	            			
            			//2��ڈȍ~��Start
            			if(hoge){
            				startTime = SystemClock.elapsedRealtime()- (stopTime - startTime);
            			}else{
            				startTime = SystemClock.elapsedRealtime()- (chrn.getStopTime() - chrn.getStartTime());
            				hoge = true;
            			}
	            		
	            		item.setStartTime(startTime);
	            		chrn.setBase(startTime);
	            		
	            		chrn.start();	
	            		btn.setText("Stop");
	            		btn.setTextColor(Color.RED);
	            		ParentTop.setBackgroundColor(rootView.getResources().getColor(R.color.list_bg_selected));
	            		
	            	}else if (btn.getText().equals("Stop")){
	            		stopTime = SystemClock.elapsedRealtime();	
	            	
	            		item.setStopTime(stopTime);
	            		
	            		chrn.stop();
	            		btn.setText("Start");
	            		btn.setTextColor(Color.BLACK);
	            		
	            		//isTmrStart=true;
	            		hoge = true;
	            		//item.setTmrIsStart(true);
	            		//chrn.setTmrIsStart(true);
	            		ParentTop.setBackgroundColor(rootView.getResources().getColor(R.color.list_bg_default));
	            		
	            		//item.setBaseTime(SystemClock.elapsedRealtime()- (stopTime - startTime));
	            	}	            	 
	         
	            	 ///////////�@Add�{�^����L���ɂ��鏈��(S)�@///////////
	             	boolean allTmrStop = false;
	            	 if (listview != null){
		            	 
		            	 for (int i = 0 ; i < listview.getChildCount() ; i++){
		            		 Button otherBtn = (Button)listview.getChildAt(i).findViewById(R.id.Button01);
		            		 
		            		 if(otherBtn != null){
		            			 
	            				 //Start������������s���ł���ꍇ�i���{�^���̕\����"Stop"�j
			            		 if (otherBtn.getText().toString().equals("Stop")){			
			            		 	break;
			            		 }
		            		 }
		            		 
		            		 if(i+1 ==listview.getChildCount() ){
		            			 allTmrStop= true;
		            		 }
		            	 }	            	 
		            	 
		            	 
		            	 for (int i = 0 ; i < listview.getChildCount() ; i++){
		            		 Button btnAdd = (Button)listview.getChildAt(i).findViewById(R.id.addtask);
		            		 Button btnReset = (Button)listview.getChildAt(i).findViewById(R.id.timereset);
		            		 //Chronometer���擾�ł�����
            				 //Start������������s���ł���ꍇ�i���{�^���̕\����"Stop"�j
		            		 if (btnAdd == null){			
		            			 continue;
		            		 }else{
		            			 if(allTmrStop){
		            				 btnAdd.setEnabled(true);
		            				 btnReset.setEnabled(true);
		            				 
		            			 }else{
		            				 btnAdd.setEnabled(false);
		            				 btnReset.setEnabled(false);
		            			 }
		            		 }
		            		 
		            	 }
	            	 
	            	 }
	            	 
	            	 
	            	 ///////////�@Add�{�^����L���ɂ��鏈��(E)�@///////////
	             
	             
	             }
	             
	             
			 });

		 
		 
		 
		 return convertView;
		 
	}

	 static class ViewHolder {
		 	
		    EditText taskname;  
		    TextView taskname2;
		    TextView category;
		    Button button;  
		    MyChronometer chronometer; 
		    Long stopTime;
		    Long startTime;
		    boolean tmrIsStart;
		    Spinner spinner;
		    int sortid;
		    //Long time;  
		    

			
		} 
	 
	 private void createCategoryList(){
			TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(this.getContext());
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			CategoryEditDao dao = new CategoryEditDao(db);
			TaskList = dao.getAllCategory();
			db.close();
			
			categorylist = new String[TaskList.size()];
			
			for(int i=0; i < TaskList.size();i++){
				
				categorylist[i] = TaskList.get(i).toString();
				
			}
	        
	 }
	 
	 public String getCategoryById(int id){
		String category="";
		 
		for(int i=0; i < TaskList.size();i++){
			CategoryInfo categoryinfo = TaskList.get(i);
			if(categoryinfo.getId() == id){
				category = categoryinfo.getCategory();
				break;
			}
			
		}
		
		//�擾�ł��Ȃ��������ԍŏ��̃J�e�S���[���Z�b�g
		if (category.equals("")){
			CategoryInfo categoryinfo = TaskList.get(0);
			category = categoryinfo.getCategory();
		}
	 
		 return category;
	 }
	 public int getIdByCategory(String category){
		int id = 0;
		 
		for(int i=0; i < TaskList.size();i++){
			CategoryInfo categoryinfo = TaskList.get(i);
			if(categoryinfo.getCategory().equals(category)){
				id = categoryinfo.getId();
				break;
			}
			
		}
		 
		 //	�擾�ł��Ȃ��������ԍŏ���ID���Z�b�g
		if(id==0){
			CategoryInfo categoryinfo = TaskList.get(0);
			id = categoryinfo.getId();
		}
		
		return id;
		
	 }
}
