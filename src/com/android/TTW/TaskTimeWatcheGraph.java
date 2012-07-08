package com.android.TTW;

import java.util.Calendar;
import java.util.List;

import com.android.TTW.TaskTimeWatcheMain.BindData;
import com.android.TTW.TaskTimeWatcheMain.GridViewOnClick;
import com.android.TTW.TaskTimeWatcheMain.MyAdapter;
import com.android.TTW.TaskTimeWatcheMain.ViewHolder;
import com.android.TTW.dao.TaskTimeDao;
import com.android.TTW.dao.TaskTimeDatabaseHelper;
import com.android.TTW.info.SchedulerInfo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class TaskTimeWatcheGraph extends Activity implements OnClickListener, OnItemSelectedListener{


	public LinearLayout layout ;
	private final static  int REQUEST_CODE = 0;
	
	//private GraphListAdapter graphListAdapater;
	private List<TaskBean> objects;
	
	
	private String[] youbiArray = {"","日","月","火","水","木","金","土"};
	private SchedulerInfo schedulerInfo;
	private Button btnAdd;
	private Button btnDate;
	private Button btnTime;
	
	
	private TimePickerDialog timePickerDialog;
	//リスト表示までのローディングダイアログ
	private ProgressDialog dialog;
	
    public class BindData {  
        int iconId;  
        String title;  
    
        BindData(int id, String s) {  
            this.iconId = id;  
            this.title = s;  
        }  
    }  
    
    private BindData[] mDatas = {
            new BindData(android.R.drawable.ic_menu_add, "円グラフ"),  
            new BindData(android.R.drawable.ic_menu_agenda, "棒グラフ"),  
            new BindData(android.R.drawable.ic_menu_manage, "折れ線グラフ"),  
            
        };  
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //レイアウト定義
//      LinearLayout layout = new LinearLayout(this);
//      setContentView(layout);
      setContentView(R.layout.graph_main);



      ListView listView = (ListView) findViewById(R.id.listview_graph);
      listView.setAdapter(new MyAdapter(this, R.layout.graph_main_item, mDatas));  
      
      // クリックイベント・リスナの登録
      listView.setOnItemClickListener(new GraphIconOnClick());
      
      
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
                holder.textView = (TextView) convertView.findViewById(R.id.text_graph);  
                holder.imageView = (ImageView) convertView.findViewById(R.id.image_graph);  
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
    
	public class GraphIconOnClick implements OnItemClickListener {
		   public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		      // 選択された画像PATHを共有領域にセットする
			   
			   
			   ParameterGraph param = new ParameterGraph();
			   
			   //TextView textview= (TextView)v.findViewById(R.id.textview);
			   //String text = textview.getText().toString();
			   View rootview = parent.getRootView();
			   Button btnDateStart = (Button)rootview.findViewById(R.id.BtnDateStart);
			   Button btnDateEnd = (Button)rootview.findViewById(R.id.BtnDateEnd);
			   
		       // チェックされているラジオボタンの ID を取得します
		       RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		       //RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
		       RadioButton radioBtnTask = (RadioButton) findViewById(R.id.radiobutton_task);
		       RadioButton radioBtnCategory= (RadioButton) findViewById(R.id.radiobutton_category);
		       String chekedItem ="";
		       
			   //SchedulerInfo scheInfoStart = convertStringToSchedulerInfo(btnDateStart.getText().toString());
			   //SchedulerInfo scheInfoEnd = convertStringToSchedulerInfo(btnDateEnd.getText().toString());
			   
			   CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox_showzerotask);
			   
			   Intent intent = null ;
			   
			   intent = new Intent(TaskTimeWatcheGraph.this,GraphDrawer.class);
			   param.setGraphType(position);
			   //param.setDateStart(scheInfoStart.toStringDateformat());
			   //param.setDateEnd(scheInfoEnd.toStringDateformat());
			   //param.setShowZeroTime(checkBox.isChecked());
		       
			   
			   
			   
			   
			   intent.putExtra("param",param);
			   
				startActivityForResult(intent,REQUEST_CODE);
			   
		   }
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

	
	
}