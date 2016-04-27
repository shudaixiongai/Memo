package com.fujitsu.memo.Acitivity;

import android.app.Activity;
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
		initView();
	}

	private void initView() {
	}

	@Override
	public void onClick(View arg0) {

	}

	public static void addFragment() {

	}

}
