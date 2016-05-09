package com.fujitsu.memo.DB;

import java.util.ArrayList;
import java.util.List;

import com.fujitsu.memo.model.Memo;
import com.fujitsu.memo.model.Type;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManger {
	private DBhelper dbHelper;
	private SQLiteDatabase db;

	// 构造函数，初始化数据

	public DBManger(Context context) {
		dbHelper = new DBhelper(context);
		db = dbHelper.getWritableDatabase();
		db = dbHelper.getReadableDatabase();
	}

	// 添加备忘录
	public void addMemo(Memo memo) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO " + DBhelper.TABLE1_NAME
				+ " (time,title,content,type) VALUES (?,?,?,?)";
		db.execSQL(
				sql,
				new Object[] { memo.getTime(), memo.getTitle(),
						memo.getContent(), memo.getType() });
		db.close();
	}

	// 删除备忘录——根据memoId
	public void deleteMemo(Integer memoId) {
		// TODO Auto-generated method stub
		db.delete(DBhelper.TABLE1_NAME, "id = ?",
				new String[] { String.valueOf(memoId) });
		db.close();
	}

	// 删除备忘录——根据type
	public void deleteMemoByType(Integer typeId) {
		// TODO Auto-generated method stub
		db.delete(DBhelper.TABLE1_NAME, "type = ?",
				new String[] { String.valueOf(typeId) });
		db.close();
	}

	// 编辑备忘录
	public void editMemo(Memo memo) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("time", memo.getTime());
		cv.put("title", memo.getTitle());
		cv.put("type", memo.getType());
		cv.put("content", memo.getContent());
		cv.put("remindtime", memo.getRemindtime());

		db.update(DBhelper.TABLE1_NAME, cv, "id = ?",
				new String[] { String.valueOf(memo.getId()) });
		db.close();
	}

	// 添加收藏或取消收藏
	// 当传入的形参addOrCancel为0时，取消收藏当addOrCancel为1时，添加收藏
	public void addOrCancelCol(Integer memoId, int addOrCancel) {
		// TODO Auto-generated method stub
		String sql = "UPDATE" + DBhelper.TABLE1_NAME + "SET collection = "
				+ addOrCancel + " WHERE id = ?";
		db.execSQL(sql, new String[] { String.valueOf(memoId) });
	}

	// 查找备忘录——根据id
	public Memo findMemoById(int memoId) {
		Memo memo = new Memo();
		Cursor cursor = db.query(DBhelper.TABLE1_NAME, new String[] { "time",
				"title", "type", "content", "collection", "remindtime" },
				"id = ?", new String[] { String.valueOf(memoId) }, null, null,
				"id desc");
		if (cursor.getCount() == 0) {
			db.close();
			return null;
		} else {
			cursor.moveToFirst();
			memo.setId(memoId);
			memo.setTime(cursor.getString(cursor.getColumnIndex("time")));
			memo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			memo.setContent(cursor.getString(cursor.getColumnIndex("content")));
			memo.setType(cursor.getInt(cursor.getColumnIndex("type")));
			memo.setCollection(cursor.getInt(cursor
					.getColumnIndex("collection")));
			memo.setRemindtime(cursor.getString(cursor
					.getColumnIndex("remindtime")));
			db.close();
			return memo;
		}
	}

	// 查找备忘录——所有
	public List<Memo> findAllMemos() {
		List<Memo> memos = new ArrayList<Memo>();
		String sql = "SELECT * FROM " + DBhelper.TABLE1_NAME
				+ " ORDER BY id desc";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() == 0) {
		} else {
			memos = memoCursorToMemos(cursor);
			for (int i = 0; i < memos.size(); i++) {
				Log.i("db mage", memos.get(i).getTitle());
			}
			db.close();
			return memos;
		}
		return memos;
	}

	// 查找备忘录——根据搜索框输入
	public List<Memo> findMemosByKey(String key) {
		ArrayList<Memo> memos = new ArrayList<Memo>();
		Cursor cursor = db.query(DBhelper.TABLE1_NAME,
				new String[] { "id", "time", "title", "content", "type",
						"collection", "remindtime" }, "title LIKE ?",
				new String[] { "%" + key + "%" }, null, null, "id desc");
		if (cursor.getCount() == 0) {
			db.close();
			return null;
		} else {
			memos = memoCursorToMemos(cursor);
			db.close();
			return memos;
		}
	}

	// 查找备忘录——根据typeid
	public List<Memo> findMemosByType(int typeId) {
		ArrayList<Memo> memos = new ArrayList<Memo>();
		Cursor cursor = db.query(DBhelper.TABLE1_NAME,
				new String[] { "id", "time", "title", "content", "type",
						"collection", "remindtime" }, "type = ?",
				new String[] { String.valueOf(typeId) }, null, null, "id desc");
		memos = memoCursorToMemos(cursor);
		db.close();
		return memos;
	}

	// 将游标内的内容装进ArrayList<Memo>
	private ArrayList<Memo> memoCursorToMemos(Cursor cursor) {
		ArrayList<Memo> memos = new ArrayList<Memo>();
		while (cursor.moveToNext()) {
			Memo memo = new Memo();
			memo.setId(cursor.getInt(cursor.getColumnIndex("id")));
			memo.setTime(cursor.getString(cursor.getColumnIndex("time")));
			memo.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			memo.setContent(cursor.getString(cursor.getColumnIndex("content")));
			memo.setType(cursor.getInt(cursor.getColumnIndex("type")));
			memo.setCollection(cursor.getInt(cursor
					.getColumnIndex("collection")));
			memo.setRemindtime(cursor.getString(cursor
					.getColumnIndex("remindtime")));
			memos.add(memo);
		}
		return memos;
	}

	// 添加类别
	public void addType(com.fujitsu.memo.model.Type type) {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO" + DBhelper.TABLE2_NAME + "VALUES(null,?)";
		db.execSQL(sql, new Object[] { type.getName() });
		db.close();
	}

	// 删除类别
	public void deleteType(int typeId) {
		deleteMemoByType(typeId);
		db.delete(DBhelper.TABLE2_NAME, "id = ?",
				new String[] { String.valueOf(typeId) });
		db.close();
	}

	// 编辑类别
	public void editType(Type type) {
		ContentValues cv = new ContentValues();
		cv.put("name", type.getName());
		db.update(DBhelper.TABLE2_NAME, cv, "id = ?",
				new String[] { String.valueOf(type.getId()) });
		db.close();
	}

	// 查找类别——根据id
	public Type findTypeById(int typeId) {
		Type type = new Type();
		Cursor cursor = db.query(DBhelper.TABLE2_NAME, new String[] { "name" },
				"id = ?", new String[] { String.valueOf(typeId) }, null, null,
				null);
		if (cursor.getCount() == 0) {
			db.close();
			return null;
		} else {
			cursor.moveToFirst();
			type.setId(typeId);
			type.setName(cursor.getString(cursor.getColumnIndex("name")));
			db.close();
			return type;
		}
	}

	// 查找类别——所有
	public List<Type> findAllTypes() {
		ArrayList<Type> types = new ArrayList<Type>();
		String sql = "SELECT * FROM " + DBhelper.TABLE2_NAME;
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() == 0) {
			db.close();
			return null;
		} else {
			types = typeCursorToTypes(cursor);
			db.close();
			return types;
		}
	}

	// 将游标内的内容装进ArrayList<Type>
	private ArrayList<Type> typeCursorToTypes(Cursor cursor) {
		ArrayList<Type> types = new ArrayList<Type>();
		while (cursor.moveToNext()) {
			Type type = new Type();
			type.setId(cursor.getInt(cursor.getColumnIndex("id")));
			type.setName(cursor.getString(cursor.getColumnIndex("name")));
			types.add(type);
		}
		return types;
	}
}
