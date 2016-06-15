package com.list.data;

import java.util.Date;

public class Mydata {
	
	//存储数据的类
	private  String num;
	private Date date;//时间
	public Mydata() {
		// TODO Auto-generated constructor stub
	}
	public Mydata(String num,Date date)
	{
		this.num = num;
		this.date = date;
	}
	public String getDate()
	{
		return date.toLocaleString();
	}
	public String getNum()
	{
		return num;
	}
	public String toString()
	{
		return "序号与日期"+num+date.toLocaleString();
	}
}
