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
		
        // アラートダイアログを設定します
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("編集");
        //alertDialogBuilder.setMessage(textview.getText().toString());
        
        edittext = new EditText(this);
        edittext.setText(caterogyIf.getCategory());
        alertDialogBuilder.setView(edittext);
       
        //id = caterogyIf.getId();
        
        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setPositiveButton("更新",
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
        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setNeutralButton("削除",
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

	
	public void onClickToEditor(View v){
		Intent intent = new Intent(CategoryEditor.this,TaskTimeWatcheEditor.class);
		//intent.putExtra("scheduleId", info.getId());
		startActivityForResult(intent,REQUEST_CODE);

	}
	
	public void onClickNewCategory(View v){
		//Toast.makeText(this, "onClickCategoryItem",  Toast.LENGTH_SHORT).show();
		
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // アラートダイアログのタイトルを設定します
        alertDialogBuilder.setTitle("編集");
        
        // アラートダイアログのメッセージを設定します
        //alertDialogBuilder.setMessage(textview.getText().toString());
        
        edittext = new EditText(this);
        edittext.setText("(カテゴリー)");
        alertDialogBuilder.setView(edittext);
        Activity activity = (Activity) v.getContext();
        //LinearLayout linearLayout = (LinearLayout)v.getParent();
        ListView listview = (ListView)activity.findViewById(R.id.list_category);
        
 
        final ArrayAdapter<CategoryInfo> adapter = arrayAdapter;
        final int listsize = listview.getChildCount();
        // アラートダイアログの肯定ボタンがクリックされた時に呼び出されるコールバックリスナーを登録します
        alertDialogBuilder.setPositiveButton("追加",
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
	
/*	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub

		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listlayout,items);
        listView.setAdapter(arrayAdapter);
		
        
		Context context = getApplicationContext();
		CharSequence text = "戻ってきました";
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
			   // 初期化子（コンストラクタの代わり）  
			   {  
			    mInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);  
			   }  
			   // 表示する「一行分のView」を返すメソッド  
			   @Override  
			   public View getView(int position, View convertView, ViewGroup parent) {  
			    // 表示する一行分のViewには、android.R.layout.simple_list_item_2  
			    // （中身は TwoLineListItem） を利用する  
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
