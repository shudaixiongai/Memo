package com.fujitsu.memo.Util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	public void showToast(Context context, String msg) {
		Toast makeText = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		makeText.show();
	}
}
