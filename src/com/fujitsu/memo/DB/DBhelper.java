package com.fujitsu.memo.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * 管理维护数据库基类
 */
public class DBhelper extends SQLiteOpenHelper {

	public DBhelper(Context context, String DB_NAME, CursorFactory factory,
			int version) {
		super(context, DB_NAME, null, version);
		// TODO Auto-generated constructor stub
	}

	public DBhelper(Context context) {
		super(context, DB_NAME, null, version);
		// TODO Auto-generated constructor stub
	}

	private static final String DB_NAME = "note.db";// 数据库名称
	private static final int version = 1;// 数据库版本

	// 创建数据库时调用此方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建memo表
		Log.i("db", "创建了数据库");
		String sql = "CREATE TABLE memo (id	INTEGER NOT NULL,time	date,title	varchar（255）,type	int（10）,content	varchar（21845）,collection	boolean,remindtime	date,PRIMARY KEY(id));";
		db.execSQL(sql);
		// 创建 type表
		// String type_sql =
		// "create table type(id INTEGER NOT NULL,name varchar(255) NOT NULL,PRIMARY KEY(id))";
		// db.execSQL(type_sql);
	}

	// 版本更行时调用
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
