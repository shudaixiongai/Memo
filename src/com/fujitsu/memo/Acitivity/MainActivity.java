package com.fujitsu.memo.Acitivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.fujitsu.memo.DB.DBManger;
import com.fujitsu.memo.DB.DBhelper;
import com.fujitsu.memo.model.Memo;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	public TextView tv_add, tv_menu,tv_time,tv_title,tv_content,tv_collection,tv_delete;
	public EditText et_search;
	SQLiteDatabase db;
	DBhelper dBhelper;
	ListView lv_listview;
	DBManger dbManger;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.main);
		initView();
        listener();
	}



	private void initView() {
		tv_add = (TextView) findViewById(R.id.tv_add);
		tv_menu = (TextView) findViewById(R.id.tv_menu);
		tv_time=(TextView)findViewById(R.id.tv_time);
		tv_title=(TextView)findViewById(R.id.tv_title);
		tv_content=(TextView)findViewById(R.id.tv_content);
		tv_collection=(TextView)findViewById(R.id.tv_collection);
		tv_delete=(TextView)findViewById(R.id.tv_delete);
		et_search = (EditText) findViewById(R.id.et_seach);
		lv_listview = (ListView) findViewById(R.id.lv_listview);
	}
	private void listener() {
		// TODO Auto-generated method stub
		tv_add.setOnClickListener(this);
		tv_menu.setOnClickListener(this);
		et_search.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_add:
			// 添加备忘录
			Intent intent = new Intent(MainActivity.this, AddActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_menu:

		case R.id.et_seach:
			SearchActivity();

		}

	}

	// 查找
	private void SearchActivity() {
		// TODO Auto-generated method stub
		db = dBhelper.getReadableDatabase();
		Memo memo=new Memo();
		dbManger = new DBManger(this);
		String key = et_search.getText().toString();
		dbManger.findMemosByKey(key);
		// 获取到集合数据
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i=0;i<data.size();i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("id", memo.getId());
			item.put("time", memo.getTime());
			item.put("title", memo.getTitle());
			item.put("content", memo.getContent());
			//item.put("type", memo.getType());
			item.put("collection", memo.getCollection());
			//item.put("remindtime", memo.getRemindtime());
			data.add(item);
		}
		// 创建SimpleAdapter适配器将数据绑定到item显示控件上
		SimpleAdapter adapter = new SimpleAdapter(
				this,
				data,
				R.layout.main_item,
				new String[] { "time", "title", "content","collection" },
				new int[] { R.id.tv_time, R.id.tv_title, R.id.tv_content,
						R.id.tv_collection });
		// 实现列表的显示
		lv_listview.setAdapter(adapter);
	}

	

	public static void addFragment() {

	}

}
