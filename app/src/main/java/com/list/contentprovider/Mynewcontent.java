package com.list.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class Mynewcontent extends ContentProvider {
	//重新的一个contentProvider
	public static final String authority = "com.list.content";
	public static final Uri CONTENT_URI = Uri.parse("content://com.list.content/elements");
	public static final int ONROW = 1;
	public static final int ALLROW = 2;
	//数据库的各种表名。
	public static final String NUM = "num";//列名
	public static final String DATE = "date";
	
	private Myopen open;
	public static UriMatcher matcher;
	static
	{
		matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(authority, "elements",ALLROW);
		matcher.addURI(authority, "elements/#", ONROW);
	}
	public Mynewcontent() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		open = new Myopen(getContext(), Myopen.DBNAME, null, Myopen.DBVERSION);
		//给定数据库名称与数据库的版本创建数据库
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		//query的实现获取数据库实例，分解uri，使用SQLiteQueryBuilder辅助加载。
		SQLiteDatabase db = open.getWritableDatabase();
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		switch(matcher.match(uri))
		{
		case ONROW:
			String rowid = uri.getPathSegments().get(1);
			builder.appendWhere(NUM+" = "+rowid);
			
			break;
		default:break;
		}
		
		builder.setTables(Myopen.DBTABLE);
		Cursor cursor = builder.query(db, projection, selection, selectionArgs,null,null,sortOrder);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		
		//MIME 类型
		//需要分解uri分别是一行的操作，或者是整个的操作。
		switch(matcher.match(uri))
		{
		case ONROW:
		return "vnd.android.cursor.item/com.list.contentprovider";
		case ALLROW:
		return "vnd.android.cursor.dir/com.list.contentprovider";
		default:throw new IllegalArgumentException();//抛出非正常的数据的异常。
		}
		
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		
		//插入过程需要通知更新uri
		SQLiteDatabase db = open.getWritableDatabase();
		Long id = db.insert(Myopen.DBTABLE, null, values);
		if(id>-1)
		{
			Uri newuri = ContentUris.withAppendedId(uri, id);
			getContext().getContentResolver().notifyChange(newuri, null);
			return newuri;
		}
		else
		{
			return null;
		}
		
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		//删除数据获取数据库，分解uri。执行删除，返回删除数量。
		SQLiteDatabase db = open.getWritableDatabase();
		switch(matcher.match(uri))
		{
		case ONROW:
			String rowid = uri.getPathSegments().get(0);
			selection = NUM+" = "+rowid+(!TextUtils.isEmpty(selection)?" and "+selection:"")+";";
			break;
			default:break;
		}
		if(selection == null) selection ="1";
		int deleteCount = db.delete(Myopen.DBTABLE, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return deleteCount;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		//同样获取数据库，调整selection 执行更新曹组o
		SQLiteDatabase db = open.getWritableDatabase();
		switch(matcher.match(uri))
		{
		case ONROW:
			String rowid = uri.getPathSegments().get(0);
			selection = NUM+" = "+rowid+(!TextUtils.isDigitsOnly(selection)?" and "+selection:"")+";";
			break;
		default:break;
		}
		int updateCount = db.update(Myopen.DBTABLE, values,selection,null);
		getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;
	}
	public static class Myopen extends SQLiteOpenHelper
	{
		public static final String DBNAME = "ddd.db";
		public static final String DBTABLE = "TABLE";
		public static final int DBVERSION = 1;
		public static final String CREATE = "create table "+DBTABLE+" ("+NUM+" varchar(40),"+DATE+" varchar(40));";
		
		public Myopen(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			//执行创建数据库的表
			db.execSQL(CREATE);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			//执行表的更新
			db.execSQL("drop table if exists"+DBTABLE);
			onCreate(db);
		}
		
	}
}
