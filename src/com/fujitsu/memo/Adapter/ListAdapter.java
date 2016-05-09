package com.fujitsu.memo.Adapter;

import java.util.List;

import com.fujitsu.memo.Acitivity.R;
import com.fujitsu.memo.Acitivity.SlideListView2;
import com.fujitsu.memo.model.Memo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListAdapter extends BaseAdapter {
	// 得到一个LayoutInflater对象来导入布局
	private LayoutInflater inflater;
	private List<Memo> lists;
	private Context context;
	private SlideListView2 listView2;

	public ListAdapter(Context context, List<Memo> memos,
			SlideListView2 lv_host_list) {
		this.inflater = LayoutInflater.from(context);
		this.lists = memos;
		this.listView2 = lv_host_list;
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.host_item, null);
			viewHolder = new ViewHolder();
			viewHolder.time = (TextView) arg1.findViewById(R.id.host_name);
			viewHolder.title = (TextView) arg1.findViewById(R.id.host_title);
			viewHolder.collection = (RelativeLayout) arg1
					.findViewById(R.id.delete2);
			viewHolder.delete = (RelativeLayout) arg1.findViewById(R.id.other2);
			arg1.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		viewHolder.time.setText(lists.get(arg0).getTime().toString());
		viewHolder.title.setText(lists.get(arg0).getTitle().toString());
		viewHolder.collection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listView2.slideBack();
			}
		});
		viewHolder.delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listView2.slideBack();
			}
		});
		return arg1;
	}

	public class ViewHolder {
		private TextView time;
		private TextView title;
		private ImageView iv_sound;
		private ImageView iv_picture;
		private RelativeLayout delete;
		private RelativeLayout collection;
	}
}
