package com.fujitsu.memo.Acitivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	public TextView tv_add, tv_menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.main);
		initView();
	}

	private void initView() {
		tv_add = (TextView) findViewById(R.id.tv_add);
		tv_menu = (TextView) findViewById(R.id.tv_menu);
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

		}

	}

	public static void addFragment() {

	}

}
