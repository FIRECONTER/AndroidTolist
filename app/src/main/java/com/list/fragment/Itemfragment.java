package com.list.fragment;

import com.example.androidmytodolist.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.EditText;
//包含与activity 交互的fragment。内部提供一个公共接口。
public class Itemfragment extends Fragment {
	private ItemListener ac;
	public interface ItemListener
	{
		public void changeitemcontent(String str);
	}
	public Itemfragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		ac = (ItemListener)activity;//获取activity 的实例
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//创建视图
		View myview = (View)inflater.inflate(R.layout.itemfragment, container, false);
		final EditText ed1 = (EditText)myview.findViewById(R.id.ed1);//获取编辑的控件
		ed1.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				//监听按键的响应
				if(event.getAction() == KeyEvent.ACTION_DOWN)
				{
					//按键按下
					if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER||keyCode == KeyEvent.KEYCODE_ENTER)
					{
						String str = ed1.getText().toString();
						ed1.setText("");//空字符
						ac.changeitemcontent(str);
					}
					
				}
				return false;
			}
		});
		return myview;
	}
	
	
}
