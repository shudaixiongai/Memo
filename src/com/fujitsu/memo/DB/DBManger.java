package com.fujitsu.memo.DB;

import java.util.List;

import com.fujitsu.memo.Acitivity.AddActivity;
import com.fujitsu.memo.model.Memo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManger {
	private DBhelper dBhelper;
	private SQLiteDatabase db;

	public DBManger(Context context) {
	}

	// 添加备忘录到list中
	private void addmemo(List<Memo> memos) {

	}

	// 查询备忘录
	private List<Memo> query() {
		return null;

	}

	// 添加类别
	private void addtype() {
	}

	// 根据类别查询备忘录
	private void querytype() {

	}
}
