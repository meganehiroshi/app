package com.android.TTW;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.RangeCategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.AbstractChart;


import com.android.TTW.dao.*;
import com.android.TTW.info.*;
import com.android.TTW.util.OriginalUtil;
import com.android.TTW.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class GraphDrawer extends Activity implements OnClickListener{


	private ListView listView;;
	private final static  int REQUEST_CODE = 0;
	
	private List<CategoryInfo> TaskList = new ArrayList<CategoryInfo>();
	
	
	public EditText edittext;
	public int id;
	
	public int[] color_items = new int[] {  Color.parseColor("#daa520")
    		,Color.parseColor("#8fbc8f")
    		,Color.parseColor("#ff7f50")
    		,Color.parseColor("#5f9ea0")
    		,Color.parseColor("#2f4f4f")
    		,Color.parseColor("#6b0000") };

	public int[] color_items_bar = new int[] {  Color.argb(255, 200, 100, 123)
    		,Color.argb(200, 200, 100, 123)
    		,Color.argb(150, 200, 100, 123)
    		,Color.argb(100, 200, 100, 123)
    		,Color.argb(50, 200, 100, 123)
    		,Color.argb(15, 200, 100, 123) };	
	
	//�p�����[�^�擾�p�ϐ�
	/*
	private int graphType;
	private String dateStart;
	private String dateEnd;
	private String groupby;
	*/

	private static ParameterGraph param;
	private static int graphType;

	protected GraphicalView mChartView;
	private String[] youbiArray = {"","��","��","��","��","��","��","�y"};
	private DatePickerDialog datePickerDialog;
	
    // ���A�܂��͍��������ς��ɍL����
    static final int WC = LinearLayout.LayoutParams.WRAP_CONTENT;
    // �W���X�g�̃T�C�Y�ɂ���
    static final int FP = LinearLayout.LayoutParams.FILL_PARENT;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.graph_drawer);

		// ******************************�@��ʂ̌����ɂ�鏈���@************************************//
		Configuration config = getResources().getConfiguration();
		// Landscape(����)
		if(config.orientation == Configuration.ORIENTATION_LANDSCAPE) { 
		    Toast toast = Toast.makeText(this, "Landscape", Toast.LENGTH_SHORT);
		    toast.show();

		    //LinearLayout top = (LinearLayout) findViewById(R.id.top);
		    //top.setOrientation(LinearLayout.HORIZONTAL);	
		    
		    LinearLayout draw_main = (LinearLayout) findViewById(R.id.drawer_main);
		    draw_main.setOrientation(LinearLayout.HORIZONTAL);	
		    
		    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(WC, FP);
	     	//layoutParams.setMargins(10, 10, 10, 10);
		    draw_main.setLayoutParams(layoutParams);

		    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, WC, 5.0f);
		    LinearLayout chart_area = (LinearLayout) findViewById(R.id.chart_area);
		    chart_area.setLayoutParams(layoutParams2);
		    
		    
		    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(0, WC, 4.0f);
		    LinearLayout input_area = (LinearLayout) findViewById(R.id.input_area);
		    input_area.setLayoutParams(layoutParams3);

		    LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(FP, WC);
		    LinearLayout date_area = (LinearLayout) findViewById(R.id.date_area);
		    date_area.setLayoutParams(layoutParams4);
		    date_area.setOrientation(LinearLayout.VERTICAL);

		} 
		// Portrait(�c��)
		else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
		    Toast toast = Toast.makeText(this, "Portrait", Toast.LENGTH_SHORT);
		    toast.show();
		    
		    LinearLayout draw_main = (LinearLayout) findViewById(R.id.drawer_main);
		    draw_main.setOrientation(LinearLayout.VERTICAL);	
		    
		    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(FP, FP);
	     	//layoutParams.setMargins(10, 10, 10, 10);
		    draw_main.setLayoutParams(layoutParams);
		    
		    LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(WC, 0);

		    LinearLayout chart_area = (LinearLayout) findViewById(R.id.chart_area);
		    //chart_area.setLayoutParams(layoutParams2);
		    
		    LinearLayout date_area = (LinearLayout) findViewById(R.id.date_area);
		    //date_area.setLayoutParams(layoutParams2);
		}
		// ******************************�@��ʂ̌����ɂ�鏈���@************************************//		
		
		
        //���t�_�C�A���O�{�^���i���j�Ɍ��ݓ���\��������
        Button BtnDateStart = (Button) findViewById(R.id.BtnDateStart);
        SchedulerInfo nowdate = getCurrentDateTimeStartOfMonth();
        BtnDateStart.setText(
        		String.format("%04d�N%02d��%02d��",
        				nowdate.getYear(),
        				nowdate.getMonth(),
        				nowdate.getDay()));
      
        //���t�_�C�A���O�{�^���i���j�Ɍ��ݓ���\��������    
  		Button BtnDateEnd = (Button) findViewById(R.id.BtnDateEnd);
  		SchedulerInfo dateEndofMonth = getCurrentDateTimeEndOfMonth();
  		BtnDateEnd.setText(
  				String.format("%04d�N%02d��%02d��",
  						dateEndofMonth.getYear(),
  						dateEndofMonth.getMonth(),
  						dateEndofMonth.getDay()));

        //�O��ʂ���̃p�����[�^�擾
  		graphType = ((ParameterGraph)getIntent().getSerializableExtra("param")).getGraphType();
  		
  		param = setParameter();
		

		
        switch(param.getGraphType()){
        case ParameterGraph.CHART_CIRCLE:
        	drawCircleChart();
        	break;
        case ParameterGraph.CHART_BAR:
        	drawBarChart();
        	break;
        case ParameterGraph.CHART_LINE:
        	drawLineChart();
        	break;
        }

  		
	}
	
	public void drawCircleChart(){
		
		TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(getBaseContext());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		GraphDao dao = new GraphDao(db);
		
		List<GraphInfo> result = dao.getGraphDataCircle(param);
		
		long totaltime = dao.getTotalTime(param) ;
		db.close();
		
		OriginalUtil orgutil = new OriginalUtil();
		
		//setContentView(R.layout.graph_drawer);
	    LinearLayout chart_area = (LinearLayout) findViewById(R.id.chart_area);
	    //TextView title = (TextView) findViewById(R.id.textview_title);
	    
	    CategorySeries series = new CategorySeries(null);
	    DefaultRenderer renderer = new DefaultRenderer();

	    //title.setText("�W�v���ԁF" + param.getDateStart() + " �` " + param.getDateEnd() + "\n���v���ԁF" + orgutil.convMills(totaltime));
	    //renderer.setChartTitle(dateStart + " - " + dateEnd + " (" + groupby + ")  ���v���ԁF" + orgutil.convMills(totaltime));
		renderer.setLabelsTextSize(15); //���x���̕����T�C�Y		
		//	      renderer.setShowLabels(false); //���x����\�����邩
		//renderer.setLegendTextSize(15); //�}��̕����T�C�Y
			      renderer.setShowLegend(false);  //�}���\�����邩

	    		
		int[] colors = createColorArray(result);
	   
  
	    
	    DecimalFormat frmtPercent = new DecimalFormat("##0.00%");
	    
	    for(int i = 0 ; i < result.size() ; i++ ){
	    	GraphInfo graphInfo = result.get(i);
	    	String percent = frmtPercent.format((double)graphInfo.getTimesum() / (double)totaltime);
	    	
	    	if(param.getGroupbyTyep() == ParameterGraph.GROUPBY_TASK){
	    		series.add(graphInfo.getTaskname() + "�F" + percent , graphInfo.getTimesum());	
	    	}else if(param.getGroupbyTyep() == ParameterGraph.GROUPBY_CATEGORY){
	    		series.add(graphInfo.getCategoryname() + "�F" + percent, graphInfo.getTimesum());
	    	}
		    
		    
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
	    }
	    
		
	    chart_area.removeAllViews();
        GraphicalView pie_chart = ChartFactory.getPieChartView(this, series, renderer);
        chart_area.addView(pie_chart);
	}
	

	@SuppressWarnings("deprecation")
	public void drawBarChart(){
		//setContentView(R.layout.graph_drawer);
		LinearLayout chart_area = (LinearLayout) findViewById(R.id.chart_area);
		
		//�O���t�̒��̔w�i�F
		chart_area.setBackgroundColor(Color.parseColor("#007070"));
		
		//�f�[�^�̎擾
		TaskTimeDatabaseHelper dbHelper = new TaskTimeDatabaseHelper(getBaseContext());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		GraphDao dao = new GraphDao(db);
		
		//�J�e�S���f�[�^�̎擾
		CategoryEditDatabaseHelper dbHelper2 = new CategoryEditDatabaseHelper(getBaseContext());
		SQLiteDatabase db2 = dbHelper2.getWritableDatabase();
		CategoryEditDao dao2 = new CategoryEditDao(db);
	
		
		List<GraphInfo> result = dao.getGraphDataBarStacke(param);
		List<CategoryInfo> resultCategory = dao2.getAllCategory();
		
		//�J���[�̃Z�b�g
		int[] colors = createColorArray(resultCategory);
		
		//
		OriginalUtil orgutil = new OriginalUtil();
		
	     //�f�[�^�Z�b�g   
			XYMultipleSeriesDataset myData = new XYMultipleSeriesDataset();
			
			XYMultipleSeriesRenderer myRenderer = new XYMultipleSeriesRenderer(); 
			
			RangeCategorySeries[] dataSeriesArray = new RangeCategorySeries[resultCategory.size()];
			XYSeriesRenderer[] rendererArray = new XYSeriesRenderer[resultCategory.size()];
			
			for (int i = 0 ; i < resultCategory.size() ; i ++){
				CategoryInfo info = (CategoryInfo)resultCategory.get(i);
				dataSeriesArray[i] = new RangeCategorySeries(String.valueOf(info.getCategory()));
				rendererArray[i] = new XYSeriesRenderer();
				rendererArray[i].setColor(colors[i]);
				myRenderer.addSeriesRenderer(i,rendererArray[i]);
			}
			
			
			
			//dataSeries.setTitle("asfasf");
			
			//NumberFormat format = NumberFormat.getInstance();
			//format.setMaximumFractionDigits(1); //�����_����
			myRenderer.clearXTextLabels();
			
			
			
			double ymaxheigt = 4 ;
			String tmpCreatetime = "";
			double tmpTimehours =0;
			//int j =0;
			int k =0;
			for (int i = 0 ; i < result.size() ; i++ ){
				GraphInfo graphInfo = result.get(i);
				//dataSeries.
				double timehours = orgutil.convMillssToHours(graphInfo.getTimesum());
				
				if(tmpCreatetime.equals(graphInfo.getCreatetime())){
				
				}else{
					k = k+1;
					tmpTimehours =0;
					tmpCreatetime = graphInfo.getCreatetime();
					myRenderer.addXTextLabel(k, convDateLabel(graphInfo.getCreatetime()));
					
					//createtime������̃����Z�b�g�̃f�[�^��(0,0)�ň�x�쐬���Ă���
					for (int j = 0 ; j < dataSeriesArray.length ; j++){
						dataSeriesArray[j].add(0, 0);						
					}
					
				}
				
				for (int j = 0 ; j < dataSeriesArray.length ; j++){
					if(dataSeriesArray[j].getTitle().equals(graphInfo.getCategoryname())){
						
						//��ԏ�̃f�[�^���폜���Ă���l��ǉ�����
						int index = dataSeriesArray[j].getItemCount();
						dataSeriesArray[j].remove(index-1);
						dataSeriesArray[j].add(tmpTimehours, (tmpTimehours * 10 + timehours * 10 )/10);
						
						if (ymaxheigt < tmpTimehours + timehours){
							ymaxheigt = tmpTimehours + timehours;
						}
					}
					
				}
				
				tmpTimehours = (tmpTimehours * 10 + timehours * 10 )/10;
				
				
				//dataSeries.setTitle(graphInfo.getCreatetime());

			}
					
			//myData.addSeries(dataSeries.toXYSeries());
			//myData.addSeries(1,dataSeries2);
			//myData.addSeries(dataSeries3.toXYSeries());
			
			for (int j = 0 ; j < dataSeriesArray.length ; j++){
				myData.addSeries(dataSeriesArray[j].toXYSeries());
			}
			
			myRenderer.setXLabels(0);
						
			
			//myRenderer.setClickEnabled(true);
			
			
			
			// XY�i�����\���́H�j�ő�ŏ��l
			myRenderer.setXAxisMin(0);
			myRenderer.setXAxisMax(6);
			myRenderer.setYAxisMin(0);
			myRenderer.setYAxisMax(ymaxheigt + 1.5);
			
			// �f�[�^�l�̕\��
			myRenderer.setDisplayChartValues(true);
			myRenderer.setChartValuesTextSize(18);
			myRenderer.setShowLabels(true);
			myRenderer.setLabelsColor(R.color.third);
			myRenderer.setBarSpacing(1);
			
			// �O���b�h�\��
			myRenderer.setShowGrid(true);
			// �O���b�h�F
			myRenderer.setGridColor(Color.parseColor("#c9c9c9"));
			
			// �X�N���[������(X,Y)
			myRenderer.setPanEnabled(true, false);
			// �X�N���[�����iX�ŏ�, X�ő�, Y�ŏ�, Y�ő�j
			myRenderer.setPanLimits(new double[]{0,  k + 1.5 , 0, 0});

			// �}��\��
			myRenderer.setShowLegend(true);
			
			// ���x���\��
			//myRenderer.setXLabels(100);
			//myRenderer.setYLabels(20);
			
			myRenderer.setBackgroundColor(R.color.bg_gray);
			
			//�\�̏�ɕ\�������^�C�g��
			//myRenderer.setChartTitle("�^�C�g���P");
			
			//�\�̉��iX���j�ɕ\�������
			//myRenderer.setXTitle("�^�C�g���Q");
			
			//�\�̍��iY���j�ɕ\�������
			myRenderer.setYTitle("����(h)",0);
			
			
			myRenderer.setLabelsTextSize(20);
			myRenderer.setYLabelsAlign(Align.RIGHT);
			
			//���x���̊p�x
			myRenderer.setXLabelsAngle(20f);
			
			//double[] range ={0,50,0,50};
			//myRenderer.setInitialRange(range);
			
			// XY���\��
			myRenderer.setShowAxes(false);
			// �o�[�Ԃ̊Ԋu
			myRenderer.setBarSpacing(1);
			// �Y�[������
			myRenderer.setZoomEnabled(false, false);
			// �]��
			int[] margin = {20, 50, 50, 30};
			myRenderer.setMargins(margin);
			// �]���w�i�F
			myRenderer.setMarginsColor(Color.parseColor("#007070"));
			
			//myRenderer.setApplyBackgroundColor(true);
			//myRenderer.setBackgroundColor(Color.RED);
			//yRenderer.setLabelsColor(Color.RED);
			
			
			
			
			mChartView  = ChartFactory.getRangeBarChartView(this, myData,myRenderer, BarChart.Type.STACKED);
			//mChartView.setBackgroundColor(R.color.secondary);
			chart_area.removeAllViews();
			chart_area.addView(mChartView);
			chart_area.setOnClickListener(new View.OnClickListener() {
		        @Override
		        public void onClick(View v) {
		          SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
		          double[] xy = mChartView.toRealPoint(0);
		          if (seriesSelection == null) {
		        	  Toast.makeText(v.getContext(), "ss",  Toast.LENGTH_SHORT).show();
		          } else {
		        	  Toast.makeText(v.getContext(), "sss",  Toast.LENGTH_SHORT).show();
		          }
		        }
		      });
			
	}
	
	
	public void drawLineChart(){
		
		setContentView(R.layout.graph_drawer);
		LinearLayout chart_area = (LinearLayout) findViewById(R.id.chart_area);
		
		
	     //�f�[�^�Z�b�g   
		XYMultipleSeriesDataset myData = new XYMultipleSeriesDataset();
		XYSeries dataSeries = new XYSeries("data");

		
		dataSeries.add(1, 7);
		dataSeries.add(2, 8);
		dataSeries.add(3, 9);
		dataSeries.add(4, 8);
		dataSeries.add(5, 7);
		dataSeries.add(6, 6);
		dataSeries.add(7, 7.1);
		dataSeries.add(8, 8);
		dataSeries.add(9, 5);
		myData.addSeries(dataSeries);
		
		
		//�e�O���t���ڂ̖��O�B�����ō��ڐ������߂�  
		  String[] titles = new String[] { "Crete", "Corfu", "Thassos", "Skiathos" };  
		  
		  //X���̍쐬  
		  List<double[]> x = new ArrayList<double[]>();  
		  for (int i = 0; i < titles.length; i++) {  
		    x.add(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 });  
		  }  
		  
		  //�e���ڂ֓�������f�[�^�B����titles�ō쐬�������ڂւƓ��������B  
		  List<double[]> values = new ArrayList<double[]>();  
		  values.add(new double[] { 12.3, 12.5, 13.8, 16.8, 20.4, 24.4, 26.4, 26.1, 23.6, 20.3, 17.2,13.9 });  
		  values.add(new double[] { 10, 10, 12, 15, 20, 24, 26, 26, 23, 18, 14, 11 });  
		  values.add(new double[] { 5, 5.3, 8, 12, 17, 22, 24.2, 24, 19, 15, 9, 6 });  
		  values.add(new double[] { 9, 10, 11, 15, 19, 23, 26, 25, 22, 18, 13, 10 });  
		  
		  //�e���ڂ̃O���t�̐F  
		  int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW };  
		  
		  //�e���ڂ̃O���t�|�C���g�̃X�^�C��  
		  PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND,  
		      PointStyle.TRIANGLE, PointStyle.SQUARE };  
		  
		  //XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);  
		  XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		  int length = renderer.getSeriesRendererCount();  
		  for (int i = 0; i < length; i++) {  
		    ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);  
		  }  
		  
		  
		  
		  /*
		  //�O���t�^�C�g���AX���AY���̃^�C�g�������X���ŏ��l�A�ő�l�AY���ŏ��l�A�ő�l�̐ݒ�  
		  setChartSettings(renderer, "Average temperature", "Month", "Temperature", 0.5, 12.5, 0, 32,  
		      Color.LTGRAY, Color.LTGRAY);  
		  */
		  
		  renderer.setXLabels(12);  
		  renderer.setYLabels(10);  
		  renderer.setShowGrid(true);  
		  renderer.setXLabelsAlign(Align.RIGHT);  
		  renderer.setYLabelsAlign(Align.RIGHT);  
		  renderer.setZoomButtonsVisible(true);  
		  
		  //�\�������X����Y���̍ŏ��ő�B  
		  //������XY�Ƃ��ɍŏ���0�ɂ���ƃO���t�̃}�C�i�X�\�����Ȃ�  
		  renderer.setPanLimits(new double[] { -10, 20, -10, 40 });  
		  renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });  
		  
		  //return�pIntent�̍쐬�B������GraphicalView�ɂ��Areturn��GraphicalView�ɂ���ƁA  
		  //�e���C�A�E�g�ւ̓��ꍞ�݂��\�ƂȂ�B  
		  //���̏ꍇ�g�p���郁�\�b�h��ChartFactory.getLineChartView�ƂȂ�B  
		  //��jGraphicalView line_chart = ChartFactory.getLineChartView(context, buildDataset(titles, x, values), renderer);  
		  //�i�Ԓl��GraphicalView�֕ύX���邱��)  
		  mChartView = ChartFactory.getLineChartView(this, myData, renderer);
		  chart_area.addView(mChartView);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		param = setParameter();
        
		switch(param.getGraphType()){
        case ParameterGraph.CHART_CIRCLE:
        	drawCircleChart();
        	break;
        case ParameterGraph.CHART_BAR:
        	drawBarChart();
        	break;
        case ParameterGraph.CHART_LINE:
        	drawLineChart();
        	break;
        }
		
		//finish();
		//startActivity(getIntent());
		
		v.getRootView().invalidate();
	
		LinearLayout chart_area = (LinearLayout)findViewById(R.id.chart_area);
		chart_area.invalidate();
	}

	
	public void onClickToEditor(View v){
		Intent intent = new Intent(GraphDrawer.this,TaskTimeWatcheEditor.class);
		//intent.putExtra("scheduleId", info.getId());
		startActivityForResult(intent,REQUEST_CODE);

	}
	

	
	
	public int[] createColorArray(List<?> value){
		
		
	    int s = (value.size()  / color_items.length) + 1 ;
	    int[] colors = new int[s * color_items.length];
	    
	    int hoge =0;
	    int k =0;
	    
	    for (  ;  k < s ; k++){
	    	
	    	for( int j =0 ; j < color_items.length ; j ++){
	    		colors[j+hoge] = color_items[j];
	    	}
	    	hoge = (k+1) * color_items.length;
	    }
		
		return colors;
		
	}
	
    public boolean onOptionsItemSelected(MenuItem item){
    	
		return false;

    }
    
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	

    }
    
	private SchedulerInfo getCurrentDateTimeStartOfMonth(){
		SchedulerInfo info = new SchedulerInfo();
		
		Calendar cal = Calendar.getInstance();
		
		//���������Z�b�g
		cal.set(
				cal.get(Calendar.YEAR)
				,cal.get(Calendar.MONTH)+1
				,cal.getActualMinimum(Calendar.DATE));
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		
		info.setYear(year);
		info.setMonth(month);
		info.setDay(day);
		String youbi = youbiArray [dayOfWeek];
		
		info.setDayOfWeek(youbi);
		info.setHour(hour);
		info.setMinute(minute);
		
		return info;
		
	}
	private SchedulerInfo getCurrentDateTimeEndOfMonth(){
		SchedulerInfo info = new SchedulerInfo();
		
		Calendar cal = Calendar.getInstance();
		
		//���������Z�b�g
		cal.set(
				cal.get(Calendar.YEAR)
				,cal.get(Calendar.MONTH)+1
				,cal.getActualMaximum(Calendar.DATE));
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		
		info.setYear(year);
		info.setMonth(month);
		info.setDay(day);
		String youbi = youbiArray [dayOfWeek];
		
		info.setDayOfWeek(youbi);
		info.setHour(hour);
		info.setMinute(minute);
		
		return info;
		
	}

	public void onClickDateBtn(View view) {
		// TODO Auto-generated method stub
		
		final Button btnDate = (Button) findViewById(view.getId());
		final SchedulerInfo schedulerInfo = convertStringToSchedulerInfo(btnDate.getText().toString());

		OnDateSetListener dateListener  = new OnDateSetListener(){
			public void onDateSet(DatePicker view,int year,int month,int day){
				String youbi= getYoubi(year,month,day);
				schedulerInfo.setYear(year);
				schedulerInfo.setMonth(month+1);
				schedulerInfo.setDay(day);
				String datestring = String.format("%04d�N%02d��%02d��",
						schedulerInfo.getYear(),
						schedulerInfo.getMonth(),
						schedulerInfo.getDay()
						);
				
				btnDate.setText(datestring);
				
				onClick(view);

			}
		};
			
		datePickerDialog = new DatePickerDialog(this,dateListener,
				schedulerInfo.getYear(),
				schedulerInfo.getMonth()-1,
				schedulerInfo.getDay());
		//���t�_�C�A���O��\��
		datePickerDialog.show();
	}
	private SchedulerInfo convertStringToSchedulerInfo(String value) {
		SchedulerInfo schedulerInfo = new SchedulerInfo();
		
		try{
			schedulerInfo.setYear(Integer.parseInt(value.substring(0, 4)));
			schedulerInfo.setMonth(Integer.parseInt(value.substring(5, 7)));
			schedulerInfo.setDay(Integer.parseInt(value.substring(8, 10)));
		}catch (Exception e) {
			// TODO: handle exception
		}

		
		return schedulerInfo;
	}
	private String getYoubi(int year,int month,int day){
		Calendar cal = Calendar.getInstance();
		cal.set(year,month,day);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		String youbi = youbiArray[dayOfWeek];
		return youbi ;
		
		
	}
	
	private ParameterGraph setParameter(){
		ParameterGraph param = new ParameterGraph();
		
		   
		   Button btnDateStart = (Button)findViewById(R.id.BtnDateStart);
		   Button btnDateEnd = (Button)findViewById(R.id.BtnDateEnd);
		   
	       // �`�F�b�N����Ă��郉�W�I�{�^���� ID ���擾���܂�
	       RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
	       //RadioButton radioButton = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
	       RadioButton radioBtnTask = (RadioButton) findViewById(R.id.radiobutton_task);
	       RadioButton radioBtnCategory= (RadioButton) findViewById(R.id.radiobutton_category);
	       String chekedItem ="";
	       
		   SchedulerInfo scheInfoStart = convertStringToSchedulerInfo(btnDateStart.getText().toString());
		   SchedulerInfo scheInfoEnd = convertStringToSchedulerInfo(btnDateEnd.getText().toString());
		   
		   CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox_showzerotask);
		   
		   
		   param.setDateStart(scheInfoStart.toStringDateformat());
		   param.setDateEnd(scheInfoEnd.toStringDateformat());
		   param.setShowZeroTime(checkBox.isChecked());
		   param.setGraphType(graphType);

		   if(radioGroup.getCheckedRadioButtonId() == radioBtnTask.getId()){
			   param.setGroupbyTyep(ParameterGraph.GROUPBY_TASK);
	       }else if(radioGroup.getCheckedRadioButtonId() == radioBtnCategory.getId()){
	    	   param.setGroupbyTyep(ParameterGraph.GROUPBY_CATEGORY);
	       }
		
		return param;
		
	}
	
	private String convDateLabel(String value){
		
		
		if(value == null || value.equals("")){
			return "";
		}
			
		value = value.substring(5, 16);
		
		return value;
	}
}
