package com.fujitsu.memo.Acitivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.fujitsu.memo.DB.DBManger;
import com.fujitsu.memo.Util.aboutTime;
import com.fujitsu.memo.model.Memo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

public class DetailsActivity extends Activity implements OnClickListener{
	//定义控件名称
	TextView timeTextView;
	TextView titleTextView;
	TextView contentTextView;
	Button remindButton;
	ImageButton shareButton;
	ImageButton editButton;
	ImageButton deleteButton;
	
	//全局变量以及对象
	DBManger dbManger;
	aboutTime abouttime;
	Calendar c = null;
	Memo memo;
	int memoId;
	AlarmManager alarmManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		memoId = 1;//实际要从mainactivity中传过来，这里先用1占位=====================================记得之后代入实际数值
		init();//调用初始化控件方法
		memo = dbManger.findMemoById(memoId);//实际用前一个页面传过来的id
		
		//显示memo信息到页面
		titleTextView.setText(memo.getTitle());
		timeTextView.setText(memo.getTime());
		contentTextView.setText(memo.getContent());
		remindButton.setText(memo.getRemindtime());
		
		setListener();//调用监听方法实现控件的监听
	}
	
	//初始化控件
	public void init() {
		// TODO Auto-generated method stub
		timeTextView=(TextView) findViewById(R.id.tv_details_time);
		titleTextView=(TextView) findViewById(R.id.tv_details_title);
		contentTextView=(TextView) findViewById(R.id.tv_details_content);
		remindButton=(Button) findViewById(R.id.btn_details_remind);
		shareButton=(ImageButton) findViewById(R.id.btn_details_share);
		editButton=(ImageButton) findViewById(R.id.btn_details_edit);
		deleteButton=(ImageButton) findViewById(R.id.btn_details_delete);
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
			startActivityForResult(intent, 110);//跳转到编辑页面并等待编辑页面的回传值==========================================
			
			break;
		case R.id.btn_details_share://点击分享，调用分享方法
			share();
			break;
		case R.id.btn_details_delete://点击删除，调用删除方法
			delete();
			break;
		case R.id.btn_details_remind://点击提醒按钮，调用更新提醒时间方法
			updateRemindTime();
			setAlarm();
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
		dbManger.deleteMemo(memoId);
		Intent intent = new Intent(DetailsActivity.this,MainActivity.class);
		startActivity(intent);
	}
	
	//修改提醒时间
	public void updateRemindTime() {
		// TODO Auto-generated method stub
		Map<String, Integer> systemTime = new HashMap<String, Integer>();
		systemTime=abouttime.getSystemTime();//获取当前系统时间
		DatePickerDialog datePickerDialog = new DatePickerDialog(DetailsActivity.this, 
				Datelistener, systemTime.get("year"), systemTime.get("month"), systemTime.get("day"));
		datePickerDialog.show();//显示日历选择器
	}
	
	//设置闹钟
	public void setAlarm() {
		Intent intent = new Intent(
				DetailsActivity.this,AlarmReceiver.class); // 创建Intent对象
		PendingIntent pi = PendingIntent.getBroadcast(DetailsActivity.this,  0, intent, 0); // 创建PendingIntent
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);//得到一个AlarmManager对象
		alarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(), pi); // 设置闹钟，当前时间就立即唤醒
		dbManger.updateRemindTime(abouttime.calenderToString(c), memoId);//根据memoId更新提醒时间
	}
	
	//日期选择器关闭之后的回调方法
	private DatePickerDialog.OnDateSetListener Datelistener=new DatePickerDialog.OnDateSetListener(){
		
		@Override
		public void onDateSet(DatePicker view, int myYear, int myMonth, int myDay) {
			// TODO Auto-generated method stub
			Map<String, Integer> systemTime = new HashMap<String, Integer>();
			systemTime=abouttime.getSystemTime();//获取当前系统时间
			c.set(Calendar.YEAR, myYear); // 设置闹钟的年份
			c.set(Calendar.MONTH, myMonth); // 设置闹钟的月份
			c.set(Calendar.DAY_OF_MONTH, myDay); // 设置闹钟的日期
			TimePickerDialog timePickerDialog = new TimePickerDialog(DetailsActivity.this, Timelistener, 
					systemTime.get("hour"), systemTime.get("minute"), true);
			timePickerDialog.show();//显示时间选择器
		}
	};
	
	//时间选择器关闭之后的回调方法
	private TimePickerDialog.OnTimeSetListener Timelistener=new TimePickerDialog.OnTimeSetListener(){
		
		@Override
		public void onTimeSet(TimePicker view, int hour, int minute) {
			// TODO Auto-generated method stub
			c.setTimeInMillis(c.getTimeInMillis()); // 设置Calendar对象
			c.set(Calendar.HOUR_OF_DAY, hour); // 设置闹钟小时数
			c.set(Calendar.MINUTE, minute); // 设置闹钟的分钟数
			c.set(Calendar.SECOND, 0); // 设置闹钟的秒数
		}
	};
}
