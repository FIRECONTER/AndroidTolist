package com.list.activity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.example.androidmytodolist.R;
import com.list.adapter.Myadapter;
import com.list.data.Mydata;
import com.list.fragment.Itemfragment.ItemListener;
import com.list.fragment.Myfragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
//测试用的Activity
public class Myactivity extends Activity implements ItemListener {
	private Myadapter adpter;
	private List<Mydata> list;
	public Myactivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		FragmentManager manager = getFragmentManager();//获取fragment的管理器
		list = new LinkedList<Mydata>();
		adpter = new Myadapter(Myactivity.this,R.layout.vies,list);//实现底层的数据匹配
		Myfragment fg = (Myfragment) (manager.findFragmentById(R.id.fg2));//查询fragment实现绑定数据
		fg.setListAdapter(adpter);
		
	}

	

	@Override
	public void changeitemcontent(String str) {
		// TODO Auto-generated method stub
		//被fragment调用更改视图
		//数据更新
		//打上时间戳
		list.add(0, new Mydata(str,new Date()));//添加显示的时间
		for(Mydata mh :list)
		{
			Log.w("数据",mh.toString());
		}
		adpter.notifyDataSetChanged();//通知更新数据
	}
	
}
