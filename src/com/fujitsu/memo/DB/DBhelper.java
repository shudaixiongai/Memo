package com.fujitsu.memo.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * 管理维护数据库基类
 */
public class DBhelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "note.db";// 数据库名称
	private static final int version = 1;// 数据库版本

	public DBhelper(Context contex) {
		super(contex, DB_NAME, null, version);
	}

	// 创建数据库时调用此方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建memo表
		String sql = "CREATE TABLE memo (id	INTEGER NOT NULL,time	date,title	varchar（255）,type	int（10）,content	varchar（21845）,collection	tinyint（1）,remindtime	date,PRIMARY KEY(id));";
		db.execSQL(sql);
		// 创建 type表
		String type_sql = "create table type(id INTEGER NOT NULL,name varchar(255) NOT NULL,PRIMARY KEY(id))";
		db.execSQL(type_sql);
	}

	// 版本更行时调用
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
