package com.jreader.storage;

import com.jreader.common.Constants;
import com.jreader.entity.ReadHistory;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReadStorage  {
	
	Activity currentActivity=null;
	SQLiteDatabase db=null;
	public ReadStorage(Activity activity)
	{
		currentActivity = activity;
		InitialDB();
	}
	private void InitialDB()
	{
		db = currentActivity.openOrCreateDatabase("myreader.db", Context.MODE_PRIVATE, null);
	    
		Cursor c = db.rawQuery("select * from sqlite_master where type='table' and tbl_name=?",
	    		new String[]{Constants.TABLE_READHISTORY});
	    
		if(c.getCount()==0)
			db.execSQL("CREATE TABLE "
					+ Constants.TABLE_READHISTORY
					+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, bookname NVARCHAR,path NVARCHAR,page SMALLINT)");
        c.close();
	}
	public ReadHistory QueryReadHistory(String bookName,String path)
	{
		ReadHistory history = null;
		Cursor c = db.rawQuery("select * from " + Constants.TABLE_READHISTORY
				+ " where bookname=? and path=?",
				new String[] { bookName, path });
	    
        while (c.moveToNext()) {
        	history = new ReadHistory();
        	history.setId(c.getInt(c.getColumnIndex("_id")));  
        	history.setPage(c.getInt(c.getColumnIndex("page")));  
        	history.setBookName(c.getString(c.getColumnIndex("bookname")));
        	history.setPath(c.getString(c.getColumnIndex("path")));
        }  
        c.close();
        
        return history;
	}
	public void InsertReadHistory(ReadHistory history)
	{
		if (history == null)
			return;
		db.execSQL(
				"INSERT INTO "+Constants.TABLE_READHISTORY+ " (bookname,path,page) VALUES (?, ?, 1)",
				new Object[] { history.getBookName(), history.getPath() });
	}
	
	public void UpdateReadHistory(ReadHistory history)
	{
		if (history == null)
			return;
		db.execSQL(
				"update " + Constants.TABLE_READHISTORY
						+ " set page=? where bookname=? and path=?",
				new Object[] {history.getPage(),history.getBookName(), history.getPath() });
	}
	public void DeleteReadHistory(ReadHistory history)
	{
		if (history == null)
			return;
		db.execSQL("delete from " + Constants.TABLE_READHISTORY
				+ " where bookname=? and path=?",
				new Object[] { history.getBookName(), history.getPath() });
	}
	
}
