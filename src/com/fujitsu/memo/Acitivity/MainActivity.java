package com.fujitsu.memo.Acitivity;

import java.util.ArrayList;
import java.util.List;

import com.fujitsu.memo.DB.DBManger;
import com.fujitsu.memo.model.Memo;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private LinearLayout ll_host_add;
	private SlideListView2 lv_host_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.main);
		setview();
	}

	// 初始化控件
	private void setview() {
		lv_host_list = (SlideListView2) findViewById(R.id.lv_listview);
		lv_host_list.initSlideMode(SlideListView2.MOD_RIGHT);
		ll_host_add = (LinearLayout) findViewById(R.id.ll_host_add);
		ll_host_add.setOnClickListener(this);
		getdata();
		lv_host_list.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// 添加备忘录监听
		case R.id.ll_host_add:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, AddActivity.class);
			startActivity(intent);
			break;
		}

	}

	// 数据源
	private void getdata() {
		DBManger dbManger = new DBManger(getApplicationContext());
		List<Memo> Memos;
		Memos = dbManger.findAllMemos();
		for (int i = 0; i < Memos.size(); i++) {
			Log.i("Memos", Memos.get(i).getTitle());
		}
		lv_host_list.setAdapter(new com.fujitsu.memo.Adapter.ListAdapter(this,
				Memos, lv_host_list));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Toast.makeText(getApplicationContext(), "你点击了" + arg2, 1).show();
	}
}
