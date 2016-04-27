package com.fujitsu.memo.Acitivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.fujitsu.memo.DB.DBManger;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class DetailsActivity extends Activity implements OnClickListener{
	//定义控件名称
	TextView timeTextView;
	TextView titleTextView;
	TextView contentTextView;
	Button remindButton;
	Button shareButton;
	Button editButton;
	Button deleteButton;
	
	DBManger dbManger;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		init();//调用初始化控件方法
		setListener();//调用监听方法实现控件的监听
	}
	
	//初始化控件
	public void init() {
		// TODO Auto-generated method stub
		timeTextView=(TextView) findViewById(R.id.tv_details_time);
		titleTextView=(TextView) findViewById(R.id.tv_details_title);
		contentTextView=(TextView) findViewById(R.id.tv_details_content);
		remindButton=(Button) findViewById(R.id.btn_details_remind);
		shareButton=(Button) findViewById(R.id.btn_details_share);
		editButton=(Button) findViewById(R.id.btn_details_edit);
		deleteButton=(Button) findViewById(R.id.btn_details_delete);
	}
	
	//对所有控件实行监控
	public void setListener() {
		// TODO Auto-generated method stub
		timeTextView.setOnClickListener(this);
		titleTextView.setOnClickListener(this);
		contentTextView.setOnClickListener(this);
		editButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		remindButton.setOnClickListener(this);
		shareButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.tv_details_time:
		case R.id.tv_details_title:
		case R.id.tv_details_content:
		case R.id.btn_details_edit://点击详情页面的时间，标题,内容和编辑按钮都可实现到编辑页面的跳转
			Intent intent = new Intent(DetailsActivity.this,EditActivity.class);
			startActivityForResult(intent, 110);//跳转到编辑页面并等待编辑页面的回传值
			
			break;
		case R.id.btn_details_share://点击分享，调用分享方法
			share();
			break;
		case R.id.btn_details_delete:
			delete();
			break;
		case R.id.btn_details_remind:
			updateRemindTime();
			break;
		default:
			break;
		}
	}
	
	//用插件实现备忘录的分享
	public void share(){
		
	}
	
	//备忘录的删除
	public void delete(){
		int memoId=1;//实际memoId从前一个activity中传过来，这里直接获取就好
		dbManger.deleteMemo(memoId);
	}
	
	//修改提醒时间
	public void updateRemindTime() {
		// TODO Auto-generated method stub
		Map<String, Integer> systemTime = new HashMap<String, Integer>();
		systemTime=getSystemTime();//获取当前系统时间
		DatePickerDialog datePickerDialog = new DatePickerDialog(DetailsActivity.this, 
				Datelistener, systemTime.get("year"), systemTime.get("month"), systemTime.get("day"));
		datePickerDialog.show();//显示日历选择器
		
		TimePickerDialog timePickerDialog = new TimePickerDialog(DetailsActivity.this, Timelistener, 
				systemTime.get("hour"), systemTime.get("minute"), true);
		timePickerDialog.show();//显示时间选择器
	}
	
	private DatePickerDialog.OnDateSetListener Datelistener=new DatePickerDialog.OnDateSetListener(){
		
		//日期选择器关闭之后的回调方法
		@Override
		public void onDateSet(DatePicker view, int myYear, int myMonth, int myDay) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private TimePickerDialog.OnTimeSetListener Timelistener=new TimePickerDialog.OnTimeSetListener(){
		
		//时间选择器关闭之后的回调方法
		@Override
		public void onTimeSet(TimePicker view, int hour, int minute) {
			// TODO Auto-generated method stub
			
		}
	};

	
	//获取当前系统时间
	public Map<String, Integer> getSystemTime() {
		// TODO Auto-generated method stub
		Map<String, Integer> systemTime = new HashMap<String, Integer>();
		Calendar calendar=Calendar.getInstance(Locale.CHINA);
		Integer year = calendar.get(Calendar.YEAR);
		Integer month = calendar.get(Calendar.MONTH)+1;
		Integer day = calendar.get(Calendar.DAY_OF_MONTH);
		Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
		Integer minute = calendar.get(Calendar.MINUTE);
		Integer second = calendar.get(Calendar.SECOND);
		
		systemTime.put("year", year);
		systemTime.put("month", month);
		systemTime.put("day", day);
		systemTime.put("hour", hour);
		systemTime.put("minute", minute);
		systemTime.put("second", second);
		
		return systemTime;
	}

}
