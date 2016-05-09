package com.fujitsu.memo.Acitivity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fujitsu.memo.DB.DBManger;
import com.fujitsu.memo.Util.aboutTime;
import com.fujitsu.memo.model.Memo;
import com.fujitsu.memo.model.Type;

/**
 * @author jy_dingsufu
 * 
 */
public class AddActivity extends Activity {
	public DBManger dbManger;
	public Memo memo;
	public List<Memo> list;
	public List<String> spinnerList = null;
	public List<com.fujitsu.memo.model.Type> typeList = null;
	public ArrayAdapter<String> arrayAdapter;
	public Button mBtnIamge, mBtnSubmit, mBtnAlarm;
	public EditText mEdContent, mEdTitle;
	public TextView mTvAlarm, mTvTime;
	public Spinner mSpinner;
	public static final int PICK_PIC = 0;
	public static final int DIALOG_TIME = 1;
	public static final int ID_DATEPICKER_DIALOG = 2;
	public static final int ID_TIME_DIALOG = 3;
	public int mImgViewWidth;
	float mInsertedImgWidth;
	private AlarmManager alarmManager = null;
	public static Calendar c = null;
	public static final String TAG = "com.fujitsu.memo.Acitivity.AddActivity";

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		// 显示层级式导航
		if (NavUtils.getParentActivityName(AddActivity.this) != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}

		}

		initViews();
		getTypeList();
		// 适配器
		arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spinnerList);
		// 设置样式
		arrayAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 加载适配器
		mSpinner.setAdapter(arrayAdapter);

		// 通过getViewTreeObserver获得ViewTreeObserver对象
		ViewTreeObserver vto = mEdContent.getViewTreeObserver();
		// 用于监听布局之类的变化，比如某个空间消失了
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				mEdContent.getViewTreeObserver().removeOnGlobalLayoutListener(
						this);
				mImgViewWidth = mEdContent.getWidth();
				mInsertedImgWidth = mImgViewWidth * 0.9f;
			}
		});

		mBtnIamge.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				add_img(arg0);
			}
		});

		mBtnAlarm.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			public void onClick(View arg0) {
				c = Calendar.getInstance();
				c.setTimeInMillis(System.currentTimeMillis());// 设置当前时间到Calendar中
				showDialog(ID_DATEPICKER_DIALOG);// 显示datePicker选择器
			}
		});

		mBtnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (c != null) {
					// 设置闹钟
					Intent intent = new Intent(AddActivity.this,
							AlarmReceiver.class); // 创建Intent对象
					PendingIntent pi = PendingIntent.getBroadcast(
							AddActivity.this, 0, intent, 0); // 创建PendingIntent
					alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);// 得到一个AlarmManager对象
					alarmManager.set(AlarmManager.RTC_WAKEUP,
							c.getTimeInMillis(), pi); // 设置闹钟，当前时间就立即唤醒
					// Toast.makeText(AddActivity.this,
					// "闹钟设置成功",Toast.LENGTH_LONG).show();// 提示用户
				}

				String title = mEdTitle.getText().toString().trim();
				String time = mTvTime.getText().toString();
				String alarmTime = mTvAlarm.getText().toString();
				String mContent = mEdContent.getText().toString();
				String typeName = mSpinner.getSelectedItem().toString();

				if (TextUtils.isEmpty(title)) {
					Toast.makeText(AddActivity.this, "标题不能为空！",
							Toast.LENGTH_SHORT).show();
					return;
				} else if (TextUtils.isEmpty(mContent)) {
					Toast.makeText(AddActivity.this, "内容不能为空！",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					memo = new Memo();// 新建一个Memo对象
					memo.setTitle(title);// 设定标题
					memo.setTime(time);// 设定编写的时间
					if (alarmTime != null) {
						if (c != null) {
							memo.setRemindtime(new aboutTime()
									.calenderToString(c));// 设定闹钟时间

						}
					}
					memo.setContent(mContent);// 设定内容
					// 设定type的值
					for (int i = 0; i < typeList.size(); i++) {
						Type type = typeList.get(i);
						if (type.getName().equals(typeName)) {
							memo.setType(type.getId());
							break;
						}
					}

					dbManger = new DBManger(AddActivity.this);
					dbManger.addMemo(memo);
					Intent i = new Intent(AddActivity.this, MainActivity.class);// 这边的要改下，不要忘记惹！！！
					//
					//
					//
					//
					startActivity(i);
				}

			}
		});
	}

	/**
	 * 获取组件
	 */
	private void initViews() {
		mBtnIamge = (Button) findViewById(R.id.btn_add_image);
		mBtnSubmit = (Button) findViewById(R.id.btn_add_submit);
		mBtnAlarm = (Button) findViewById(R.id.btn_add_alarms);
		mEdContent = (EditText) findViewById(R.id.ed_add_content);
		mEdTitle = (EditText) findViewById(R.id.ed_add_title);
		mTvAlarm = (TextView) findViewById(R.id.tv_add_alarm);
		mTvTime = (TextView) findViewById(R.id.tv_add_time);
		mSpinner = (Spinner) findViewById(R.id.sp_add_spinner);

		mTvTime.setText(new aboutTime().dateToString(new Date(System
				.currentTimeMillis())));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 *      为activity添加层级式导航事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(AddActivity.this) != null) {
				NavUtils.navigateUpFromSameTask(AddActivity.this);
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 获取所有类别
	 */
	private void getTypeList() {
		spinnerList = new ArrayList<String>();
		typeList = new ArrayList<com.fujitsu.memo.model.Type>();
		dbManger = new DBManger(AddActivity.this);
		typeList = dbManger.findAllTypes();
		for (int i = 0; i < typeList.size(); i++) {
			spinnerList.add(typeList.get(i).getName());
		}
	}

	/**
	 * 新增图片
	 * 
	 * @param v
	 */
	public void add_img(View v) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PICK_PIC);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == PICK_PIC) {
				if (data == null) {
					Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
				} else {
					Uri uri = data.getData();
					System.out
							.println("uri--------------------------------------------------------"
									+ uri);
					Bitmap bitmap = getOriginalBitmap(uri);
					SpannableString ss = getBitmapMime(bitmap, uri);
					insertIntoEditText(ss);
				}
			}
		}
	}

	/**
	 * EditText中可以接收的图片(要转化为SpannableString)
	 * 
	 * @param pic
	 * @param uri
	 * @return SpannableString
	 */
	private SpannableString getBitmapMime(Bitmap pic, Uri uri) {
		int imgWidth = pic.getWidth();
		int imgHeight = pic.getHeight();
		// 只对大尺寸图片进行下面的压缩，小尺寸图片使用原图
		if (imgWidth >= mInsertedImgWidth) {
			float scale = (float) mInsertedImgWidth / imgWidth;
			Matrix mx = new Matrix();
			mx.setScale(scale, scale);
			pic = Bitmap.createBitmap(pic, 0, 0, imgWidth, imgHeight, mx, true);
		}

		// 从相对路径转换成本地手机的绝对路径
		String[] proj = { MediaStore.Images.Media.DATA };
		@SuppressWarnings("deprecation")
		Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
		int actual_image_column_index = actualimagecursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String img_path = actualimagecursor
				.getString(actual_image_column_index);

		StringBuffer buffer = new StringBuffer();
		buffer.append("<img src='" + img_path + "'/>");
		buffer.append("\n");
		String smile = buffer.toString();
		System.out.println("smile+++++++++++++++++++++++++++++++++++++++"
				+ smile);
		SpannableString ss = new SpannableString(smile);
		ImageSpan span = new ImageSpan(this, pic);
		ss.setSpan(span, 0, smile.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ss;
	}

	/**
	 * 把图片插到EditText中
	 * 
	 * @param ss
	 */
	private void insertIntoEditText(SpannableString ss) {

		// 先获取Edittext中原有的内容
		Editable et = mEdContent.getText();
		if (!et.toString().equals(null) && !et.toString().equals("")) {
			if (!ss.toString().equals(null) && !ss.toString().equals("")) {

				String[] mStrings = et.toString().split("\n");// 根据换行符拆分字符串
				if (!mStrings[mStrings.length - 1].endsWith("/>")) {// 判断最后插入的是否也是一张图片
					et.append("\n");// 添加一个换行符(不是图片代表之前是字符串，所以插入一条换行符)
				}
				mEdContent.setText(et);// 重新设值到TextView中
				// 设置Edittext光标在最后显示
				mEdContent.setSelection(et.length());

			}
		}

		int start = mEdContent.getSelectionStart();
		// 设置ss要添加的位置
		et.insert(start, ss);
		// 把et添加到Edittext中
		mEdContent.setText(et);
		// 设置Edittext光标在最后显示
		mEdContent.setSelection(start + ss.length());
	}

	/**
	 * 根据照片的URI获取一个Bitmap对象
	 * 
	 * @param photoUri
	 * @return
	 */
	private Bitmap getOriginalBitmap(Uri photoUri) {
		if (photoUri == null) {
			return null;
		}
		Bitmap bitmap = null;
		try {
			ContentResolver conReslv = getContentResolver();
			// 得到选择图片的Bitmap对象
			bitmap = MediaStore.Images.Media.getBitmap(conReslv, photoUri);
		} catch (Exception e) {
			Log.e(TAG, "Media.getBitmap failed", e);
		}
		return bitmap;
	}

	@SuppressWarnings("unused")
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub

		int year = c.get(Calendar.YEAR);// 获取年份
		int month = c.get(Calendar.MONTH);// 获取月份
		int day = c.get(Calendar.DAY_OF_MONTH);// 获取日数
		int hour = c.get(Calendar.HOUR_OF_DAY);// 获取选择的时间
		int minute = c.get(Calendar.MINUTE);// 获取选择的分钟
		Dialog dialog = null;
		switch (id) {
		case ID_DATEPICKER_DIALOG:
			if (dialog != null) {
				dialog.dismiss();
			}

			dialog = new DatePickerDialog(AddActivity.this,
					new DatePickerDialog.OnDateSetListener() {

						@SuppressWarnings("deprecation")
						@Override
						public void onDateSet(DatePicker arg0, int arg1,
								int arg2, int arg3) {

							c.set(Calendar.YEAR, arg1); // 设置闹钟的年份
							c.set(Calendar.MONTH, arg2); // 设置闹钟的月份
							c.set(Calendar.DAY_OF_MONTH, arg3); // 设置闹钟的日期
							showDialog(ID_TIME_DIALOG);
						}
					}, year, month, day);
			break;
		case ID_TIME_DIALOG:
			dialog = new TimePickerDialog(AddActivity.this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker timePicker,
								int hourOfDay, int minute) {
							c.setTimeInMillis(c.getTimeInMillis()); // 设置Calendar对象
							c.set(Calendar.HOUR_OF_DAY, hourOfDay); // 设置闹钟小时数
							c.set(Calendar.MINUTE, minute); // 设置闹钟的分钟数
							c.set(Calendar.SECOND, 0); // 设置闹钟的秒数
							c.set(Calendar.MILLISECOND, 0); // 设置闹钟的毫秒数

							mTvAlarm.setText("设置的闹钟时间为：" + c.get(Calendar.YEAR)
									+ "-" + (c.get(Calendar.MONTH) + 1) + "-"
									+ c.get(Calendar.DAY_OF_MONTH) + "  "
									+ c.get(Calendar.HOUR_OF_DAY) + ":"
									+ c.get(Calendar.MINUTE));
							mTvAlarm.setVisibility(View.VISIBLE);

						}
					}, hour, minute, true);
			break;
		default:
			break;
		}
		if (dialog != null) {
			Log.i(TAG, dialog.toString());
		} else {
			Log.i(TAG, "dialog = null");
		}
		return dialog;
	}
}
