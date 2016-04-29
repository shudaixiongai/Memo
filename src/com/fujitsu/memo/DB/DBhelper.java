package com.fujitsu.memo.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * 管理维护数据库基类
 */
public class DBhelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "note.db";// 数据库名称
	private static final int version = 1;// 数据库版本
	public static final String TABLE1_NAME="memo";//表1名称
	public static final String TABLE2_NAME="type";//表2名称

	public DBhelper(Context contex) {
		super(contex, DB_NAME, null, version);
	}

	// 创建数据库时调用此方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建memo表
		String sql = "CREATE TABLE memo("
				+ "id INTEGER PRIMARY KEY,"
				+ "time varchar(128) NOT NULL,"
				+ "title varchar(255) NOT NULL,"
				+ "content varchar(21845) DEFAULT 'NULL',"
				+ "type INTEGER REFERENCES 'type' ('id') ON UPDATE CASCADE ON DELETE CASCADE,"
				+ "collection INTEGER NOT NULL DEFAULT '0',"
				+ "remindtime varchar(128) DEFAULT 'NULL')";
		db.execSQL(sql);
		
		// 创建 type表
		String type_sql = "create table type("
				+ "id INTEGER PRIMARY KEY,"
				+ "name varchar(255) NOT NULL)";
		db.execSQL(type_sql); 
		String defaultTypeString="insert into " + TABLE2_NAME + " (name) values(?)";
		db.execSQL(defaultTypeString, new String[]{"default"});
	}

	// 版本更新时调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int i, int j) {
		 db.execSQL("DROP TABLE IS EXIST"+TABLE1_NAME);  //删除表1      
		 db.execSQL("DROP TABLE IS EXIST"+TABLE2_NAME);  //删除表2      
		 onCreate(db);//重新创建数据库
	}
}
