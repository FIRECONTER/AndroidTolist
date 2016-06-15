package com.list.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
//数据交互的底层接口。
public class Myprovider extends ContentProvider {
	public static final String authority = "com.list.contentprovider";//本次的授权
	public static final Uri CONTENT_URI= Uri.parse("content://com.list.contentprovider/elements");
	public static final String KEY_ID = "ID";//主键自动递增
	public static final String KEY_TASK = "task";
	public static final String KEY_CREATION_DATE = "creation_date";
	public static final String TABLENAME = "mytable";
	private static final int SINGLE_ROW = 1;
	private static final int ALLROW = 2;
	public static final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private MyOpenHelper myopenhelper;
	static
	{
		matcher.addURI(authority, "elements", SINGLE_ROW);
		matcher.addURI(authority,"elements/#",ALLROW);//多行的查询
	}
	public Myprovider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		//初始化数据库的操作。
		myopenhelper = new MyOpenHelper(getContext(), MyOpenHelper.DBNAME, null, MyOpenHelper.VERSION);//创建数据库的辅助类
		
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		//查询的实现
		//使用QueryBuilder 实现查询。
		//首先获取一个db
		SQLiteDatabase db = myopenhelper.getWritableDatabase();
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		builder.setTables(Myprovider.TABLENAME);
		switch(matcher.match(uri))
		{
		case SINGLE_ROW:
			String rowid = uri.getPathSegments().get(1);//获取行号
			builder.appendWhere(KEY_ID+" = "+rowid);
			break;
		default:break;
		}
		Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		return cursor;
		//实现步骤，获取数据库，后去querybuilder 分析uri执行条件的添加，执行查询，返回cursor结果
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		//先写这个方法
		switch(matcher.match(uri))
		{
			//返回文件的MIME类型，即是文件的后缀与响应的打开方式的字符串。
		case SINGLE_ROW:
			return "vd.android.cursor.item/vd.padds.todos";
			
		case ALLROW:
			return "vd.android.cursor.dir/vd.padd.todos";
			//MIME的文件后缀不能出错
		default:throw new IllegalArgumentException("Uri错误");
		}
		
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		//插入的过程。获取数据库实例，不需要分解uri,执行插入返回新的uri然后拼接uri，之后通知更新。
		SQLiteDatabase db = myopenhelper.getWritableDatabase();
		//执行插入返回uri。
		Long id = db.insert(Myprovider.TABLENAME, null, values);
		if(id>-1)
		{
			//拼接uri，通知更新，返回uri
			Uri newuri = ContentUris.withAppendedId(Myprovider.CONTENT_URI, id);
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
		
		//删除操作的实现。删除是需要返回删除行数的。且需要分解uri。
		//首先还是获取db，执行uri分解，然后根据分解结果凭借selection,如果selection 为空，那么selection = "1"
		//实现的是全部的删除操作。
		//然后执行删除，返回删除的行数。
		SQLiteDatabase db = myopenhelper.getWritableDatabase();
		switch(matcher.match(uri))
		{
		case SINGLE_ROW:
			String rowid = uri.getPathSegments().get(1);
			selection = KEY_ID+" = "+rowid+(!TextUtils.isEmpty(selection)?" and "+selection:"")+";";
			break;
		default:break;
		}
		if(selection == null) selection ="1";//全部删除
		int deleteCount = db.delete(Myprovider.TABLENAME,selection,selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);//通知更新
		return  deleteCount;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		
		//update 的更新实现。获取db实例。分解uri然后执行更新。
		SQLiteDatabase db = myopenhelper.getWritableDatabase();
		switch(matcher.match(uri))
		{
		case SINGLE_ROW:
			String rowid = uri.getPathSegments().get(1);
			selection = KEY_ID+" = "+rowid+(!TextUtils.isEmpty(selection)?" and "+selection:"")+";";
			break;
			default:break;
			
		}
		int updateCount = db.update(Myprovider.TABLENAME, values, selection, null);
		getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;//返回更新的行数
	}
	public static class MyOpenHelper extends SQLiteOpenHelper
	{
		public static final String DBNAME = "mydb.db";
		public static int VERSION = 1;
		
		public MyOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			String sql = "create table "+TABLENAME+" ("+KEY_ID+" int, "+KEY_TASK+" text not null, "+KEY_CREATION_DATE+" long);";
			db.execSQL(sql);//执行创建表的操作
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("drop table if exists "+TABLENAME);
			onCreate(db);
		}
		
	}
}
