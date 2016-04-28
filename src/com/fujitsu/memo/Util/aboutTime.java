package com.fujitsu.memo.Util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class aboutTime {
	public Date strToDate(String timeString) throws ParseException {
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date timeDate=(Date) format.parse(timeString);
		return timeDate;
	}
	
	public String dateToString(Date timeDate){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String stringDate=(String) format.format(timeDate);
		return stringDate;
	}
	
	public String calenderToString(Calendar calendar){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateStr = sdf.format(calendar.getTime());
		return dateStr;
	}
	
}
