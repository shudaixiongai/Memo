package com.fujitsu.memo.Util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class aboutTime {
	//将字符串转换成时间
	public Date strToDate(String timeString) throws ParseException {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date timeDate=(Date) format.parse(timeString);
		return timeDate;
	}
	
	//将时间转换成字符串
	public String dateToString(Date timeDate){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String stringDate=(String) format.format(timeDate);
		return stringDate;
	}
	
	//将时间转换成字符串
	public String calenderToString(Calendar calendar){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateStr = sdf.format(calendar.getTime());
		return dateStr;
	}
	
	//获取当前系统时间，存入map中
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
