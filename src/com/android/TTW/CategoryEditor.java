package com.android.TTW;

import java.util.ArrayList;
import java.util.List;

import com.android.TTW.dao.*;
import com.android.TTW.info.*;
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

public class CategoryEditor extends Activity implements OnClickListener,OnItemClickListener {

	private ListView listView;
	

	private final static  int REQUEST_CODE = 0;
	
	private List<CategoryInfo> TaskList = new ArrayList<CategoryInfo>();
	
	
	public EditText edittext;
	public int id;
	public ArrayAdapter<CategoryInfo> arrayAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        setContentView(R.layout.categoryeditor);       
       
        listView = (ListView)findViewById(R.id.list_category);
        listView.setOnItemClickListener(this);
        
        
        makeListView();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(CategoryEditor.this,TaskTimeWatcheEditor.class);
		startActivityForResult(intent,REQUEST_CODE);
	}

	@Override
	public void onItemClick(AdapterView<?> view, View list_textview, int position, long id) {
		
		final TextView textview = (TextView)view.findViewById(R.id.textview_categoryItem);
		//final TextView textview = (TextView)list_textview;
		final int pos = position;
		final CategoryInfo caterogyIf = (CategoryInfo)view.getItemAtPosition(position);
		final ArrayAdapter<CategoryInfo> adapter = arrayAdapter;
		
        // �A���[�g�_�C�A���O��ݒ肵�܂�
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("�ҏW");
        //alertDialogBuilder.setMessage(textview.getText().toString());
        
        edittext = new EditText(this);
        edittext.setText(caterogyIf.getCategory());
        alertDialogBuilder.setView(edittext);
       
        //id = caterogyIf.getId();
        
        // �A���[�g�_�C�A���O�̍m��{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
        alertDialogBuilder.setPositiveButton("�X�V",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	
                    	TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(getBaseContext());
                		SQLiteDatabase db = dbHelper.getWritableDatabase();
                		CategoryEditDao dao = new CategoryEditDao(db);
                		
                		//View h = getResources()
                		//TextView tvid = (TextView) getParent().findViewById(R.id.textview_categoryId);
                 		CategoryInfo categoryInfo = new CategoryInfo();
                		categoryInfo.setId(caterogyIf.getId());
                		categoryInfo.setCategory(edittext.getText().toString());
                		
                		int result = dao.update(categoryInfo);
                		db.close();
                		
                		textview.setText(categoryInfo.getCategory());
                		adapter.notifyDataSetChanged();
                		
                		
                		//finish();
                		//startActivity(getIntent());
                    }
                });
        // �A���[�g�_�C�A���O�̍m��{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
        alertDialogBuilder.setNeutralButton("�폜",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	

                    	TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(getBaseContext());
                		SQLiteDatabase db = dbHelper.getWritableDatabase();
                		CategoryEditDao dao = new CategoryEditDao(db);
                		
                		int result = dao.delete(caterogyIf.getId());
                		db.close();
                		
                		adapter.remove(caterogyIf);
                		
                		//finish();
                		//startActivity(getIntent());
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

	
	public void onClickToEditor(View v){
		Intent intent = new Intent(CategoryEditor.this,TaskTimeWatcheEditor.class);
		//intent.putExtra("scheduleId", info.getId());
		startActivityForResult(intent,REQUEST_CODE);

	}
	
	public void onClickNewCategory(View v){
		//Toast.makeText(this, "onClickCategoryItem",  Toast.LENGTH_SHORT).show();
		
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // �A���[�g�_�C�A���O�̃^�C�g����ݒ肵�܂�
        alertDialogBuilder.setTitle("�ҏW");
        
        // �A���[�g�_�C�A���O�̃��b�Z�[�W��ݒ肵�܂�
        //alertDialogBuilder.setMessage(textview.getText().toString());
        
        edittext = new EditText(this);
        edittext.setText("(�J�e�S���[)");
        alertDialogBuilder.setView(edittext);
        Activity activity = (Activity) v.getContext();
        //LinearLayout linearLayout = (LinearLayout)v.getParent();
        ListView listview = (ListView)activity.findViewById(R.id.list_category);
        
 
        final ArrayAdapter<CategoryInfo> adapter = arrayAdapter;
        final int listsize = listview.getChildCount();
        // �A���[�g�_�C�A���O�̍m��{�^�����N���b�N���ꂽ���ɌĂяo�����R�[���o�b�N���X�i�[��o�^���܂�
        alertDialogBuilder.setPositiveButton("�ǉ�",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    	
                    	TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(getBaseContext());
                		SQLiteDatabase db = dbHelper.getWritableDatabase();
                		CategoryEditDao dao = new CategoryEditDao(db);
                		
                 		CategoryInfo categoryInfo = new CategoryInfo();
                		//categoryInfo.setId(listsize+1);
                		categoryInfo.setCategory(edittext.getText().toString());
                		
                		long result = dao.insert(categoryInfo);
                		db.close();
                		
                		adapter.add(categoryInfo);
                		adapter.notifyDataSetChanged();
                		//finish();
                		//startActivity(getIntent());
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
	
/*	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub

		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listlayout,items);
        listView.setAdapter(arrayAdapter);
		
        
		Context context = getApplicationContext();
		CharSequence text = "�߂��Ă��܂���";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
		makeListView();
	}	*/
	
	private void makeListView(){
		TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		CategoryEditDao dao = new CategoryEditDao(db);
		TaskList = dao.getAllCategory();
		db.close();
		
		/*
		String[] listArray = new String[TaskList.size()];
		for(int i=0; i < TaskList.size();i++){
			CategoryInfo info = TaskList.get(i);
			listArray[i] = info.toString();
			
		}
        */
		
		arrayAdapter = new ArrayAdapter<CategoryInfo>(this, R.layout.categoryitem, TaskList) {  
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
			    CategoryInfo s = getItem(position);  
			    convertView = mInflater.inflate(R.layout.categoryitem, null);
			    
			    TextView view = (TextView)convertView.findViewById(R.id.textview_categoryId);
			    TextView view2 = (TextView)convertView.findViewById(R.id.textview_categoryItem);
			    
			    view.setText(String.valueOf(s.getId()));  
			    view2.setText(s.getCategory());
			    
			    return convertView;  
			   }  
			  };  
		listView.setAdapter(arrayAdapter);
		
	}
	
	
}
