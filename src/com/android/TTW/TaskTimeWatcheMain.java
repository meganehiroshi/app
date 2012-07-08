package com.android.TTW;

import java.util.ArrayList;
import java.util.List;

import com.android.TTW.TaskAdapter.ViewHolder;
import com.android.TTW.dao.*;
import com.android.TTW.info.*;
import com.android.TTW.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.MailTo;
import android.net.Uri;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class TaskTimeWatcheMain extends Activity implements OnClickListener,OnItemClickListener {


	private ListView listView;
	private Button btnCreate;
	
	private final int FP = ViewGroup.LayoutParams.FILL_PARENT;
	private final int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
	
	private String[] items = {"apple","orange","banana","kiwi","mango"};
	private final static  int REQUEST_CODE = 0;
	
	private List<CategoryInfo> TaskList = new ArrayList<CategoryInfo>();
	
	
	
    public class BindData {  
        int iconId;  
        String title;  
    
        BindData(int id, String s) {  
            this.iconId = id;  
            this.title = s;  
        }  
    }  
   
    private BindData[] mDatas = {  
        new BindData(android.R.drawable.ic_menu_add, "新規計測"),  
        new BindData(android.R.drawable.ic_menu_agenda, "履歴"),  
        new BindData(android.R.drawable.ic_menu_manage, "カテゴリー編集"),  
        new BindData(android.R.drawable.ic_menu_sort_by_size, "グラフ"),  
        new BindData(android.R.drawable.ic_menu_send, "メール送信")
    };  
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
        //レイアウト定義
//        LinearLayout layout = new LinearLayout(this);
//        setContentView(layout);
        setContentView(R.layout.main);

 
 
        GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new MyAdapter(this, R.layout.main_item, mDatas));  
        
        // クリックイベント・リスナの登録
        gridView.setOnItemClickListener(new GridViewOnClick());
        
	}
    static class ViewHolder {  
        TextView textView;  
        ImageView imageView;  
    }  
	
    public class MyAdapter extends ArrayAdapter<BindData> {  
        private LayoutInflater inflater;  
        private int layoutId;  
  
        public MyAdapter(Context context, int layoutId, BindData[] objects) {  
            super(context, 0, objects);  
            this.inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
            this.layoutId = layoutId;  
        }  
  
        @Override  
        public View getView(int position, View convertView, ViewGroup parent) {  
            ViewHolder holder;  
  
            if (convertView == null) {  
                convertView = inflater.inflate(layoutId, parent, false);  
                holder = new ViewHolder();  
                holder.textView = (TextView) convertView.findViewById(R.id.textview);  
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageview);  
                convertView.setTag(holder);  
            } else {  
                holder = (ViewHolder) convertView.getTag();  
            }  
            BindData data= getItem(position);  
            holder.textView.setText(data.title);  
            holder.imageView.setImageResource(data.iconId);  
            return convertView;  
        }  
    }  
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(TaskTimeWatcheMain.this,TaskTimeWatcheEditor.class);
		startActivityForResult(intent,REQUEST_CODE);
	}

	@Override
	public void onItemClick(AdapterView<?> view, View textview, int position, long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(TaskTimeWatcheMain.this,TaskTimeWatcheEditor.class);
		//intent.putExtra("item", items[position]);
		CategoryInfo info = TaskList.get(position);
		intent.putExtra("scheduleId", info.getId());
		
		startActivityForResult(intent,REQUEST_CODE);
	}

	
	public void onClickToEditor(View v){
		Intent intent = new Intent(TaskTimeWatcheMain.this,TaskTimeWatcheEditor.class);
		//intent.putExtra("scheduleId", info.getId());
		startActivityForResult(intent,REQUEST_CODE);

	}
	
	public void onClickToMakeCategory(View v){
		Intent intent = new Intent(TaskTimeWatcheMain.this,CategoryEditor.class);
		//intent.putExtra("scheduleId", info.getId());
		startActivityForResult(intent,REQUEST_CODE);		
	}
	
	/**
	 * サムネイル・タッチ・イベントリスナ
	 */
	public class GridViewOnClick implements OnItemClickListener {
	   public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	      // 選択された画像PATHを共有領域にセットする
		   
		   TextView textview= (TextView)v.findViewById(R.id.textview);
		   String text = textview.getText().toString();
		   
		   Intent intent = null ;
		   
		   if(text.equals("新規計測")){
			   	intent = new Intent(TaskTimeWatcheMain.this,TaskTimeWatcheEditor.class);
		   }else if(text.equals("履歴")){
			   intent = new Intent(TaskTimeWatcheMain.this,TaskTimeWatcheHistory.class);
		   }else if(text.equals("カテゴリー編集")){
			   intent = new Intent(TaskTimeWatcheMain.this,CategoryEditor.class);
		   }else if(text.equals("グラフ")){
			   intent = new Intent(TaskTimeWatcheMain.this,TaskTimeWatcheGraph.class);
		   }else if(text.equals("メール送信")){
			   Uri uri=Uri.parse("mailto:");
			   intent=new Intent(Intent.ACTION_SENDTO,uri);
			   intent.putExtra(Intent.EXTRA_SUBJECT,"タイトル");
			   intent.putExtra(Intent.EXTRA_TEXT,"ボディのテキスト");
			   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			   
		   }
		   
			startActivityForResult(intent,REQUEST_CODE);
		   
	   }
	}
	
	
	private void makeListView(){
		TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(this);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		CategoryEditDao dao = new CategoryEditDao(db);
		TaskList = dao.getAllCategory();
		db.close();
		
		String[] listArray = new String[TaskList.size()];
		for(int i=0; i < TaskList.size();i++){
			CategoryInfo info = TaskList.get(i);
			listArray[i] = info.toString();
			
		}
        
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listlayout,listArray);
		listView.setAdapter(arrayAdapter);
		
	}
	
	
}
