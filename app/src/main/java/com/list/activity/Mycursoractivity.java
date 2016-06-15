package com.list.activity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.example.androidmytodolist.R;
import com.list.adapter.Myadapter;
import com.list.contentprovider.Mynewcontent;
import com.list.data.Mydata;
import com.list.fragment.Myfragment;
import com.list.fragment.Itemfragment.ItemListener;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
//使用的ContentProvider返回的数据填充视图。
public class Mycursoractivity extends Activity implements ItemListener,LoaderCallbacks<Cursor> {
	private LoaderManager manager;
	private Myadapter adpter;
	private List<Mydata> list;
	public Mycursoractivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		FragmentManager fmanager = getFragmentManager();//获取fragment的管理器
		list = new LinkedList<Mydata>();
		adpter = new Myadapter(Mycursoractivity.this,R.layout.vies,list);//实现底层的数据匹配
		Myfragment fg = (Myfragment) (fmanager.findFragmentById(R.id.fg2));//查询fragment实现绑定数据
		fg.setListAdapter(adpter);
		manager = getLoaderManager();
		manager.initLoader(0, null, this);//初始化异步的查询
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//记性重新启动查询
		manager.restartLoader(0, null, this);//重新启动异步的查询工作
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		//异步的查询过程
		CursorLoader loader = new CursorLoader(Mycursoractivity.this,Mynewcontent.CONTENT_URI,null,null,null,null);
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		//更改数据源
		//
		list.clear();
		while(data.moveToNext())
		{
			String mynum = data.getString(0);
			String mydate = data.getString(1);
			Mydata dy = new Mydata(mynum,new Date(mydate));
			list.add(0,dy);
			adpter.notifyDataSetInvalidated();//通知数据的更新
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeitemcontent(String str) {
		// TODO Auto-generated method stub
		//更改数据源
		ContentResolver re = getContentResolver();
		ContentValues va = new ContentValues();
		va.put(Mynewcontent.NUM, str);
		va.put(Mynewcontent.DATE, (new Date()).toLocaleString());
		re.insert(Mynewcontent.CONTENT_URI,va);
		manager.restartLoader(0, null, this);//重新进行异步的查询操作。
	}
	
}
