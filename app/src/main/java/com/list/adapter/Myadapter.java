package com.list.adapter;

import java.util.List;

import com.example.androidmytodolist.R;
import com.list.data.Mydata;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Myadapter extends ArrayAdapter<Mydata> {
	
	private int resource;
	public Myadapter(Context context, int textViewResourceId,
			List<Mydata> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		resource = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//adapter 核心实现视图与数据的绑定。
		LinearLayout views;//用于返回的view
		LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null)
		{
			views = new LinearLayout(getContext());
			inflater.inflate(resource, views, true);//填充视图,将布局内容填充到父视图
		}
		else
		{
			views = (LinearLayout) convertView;
		}
		Mydata data = getItem(position);//获取对应行的数据
		//填充视图的数据
		TextView shownum = (TextView)views.findViewById(R.id.shownum);
		TextView showdate = (TextView)views.findViewById(R.id.showdate);
		Log.w("num",shownum.getText().toString());//显示num
		shownum.setText(data.getNum());
		showdate.setText(data.getDate());//设定显示的视图
		return views;
	}
	

}
