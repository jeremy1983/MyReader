package com.jreader.storage;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.jreader.common.Constants;
import com.jreader.entity.SettingEntity;

public class SettingStorage {
	Activity currentActivity=null;
	SQLiteDatabase db=null;
	public SettingStorage(Activity activity)
	{
		currentActivity = activity;
		InitialDB();
	}
	private void InitialDB()
	{
		db = currentActivity.openOrCreateDatabase("myreader.db", Context.MODE_PRIVATE, null);
	    
		Cursor c = db.rawQuery("select * from sqlite_master where type='table' and tbl_name=?",
	    		new String[]{Constants.TABLE_SETTINGS});
	    
		if(c.getCount()==0)
		{
			db.execSQL("CREATE TABLE "
					+ Constants.TABLE_SETTINGS
					+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, settingkey NVARCHAR,settingvalue NVARCHAR)");

			db.execSQL(
					"INSERT INTO "+Constants.TABLE_SETTINGS+ " (settingkey,settingvalue) VALUES (?, ?)",
					new Object[] {Constants.SETTINGS_LASTDIR,""});

		}
        c.close();
	}
	public SettingEntity QuerySetting(String settingkey)
	{
		SettingEntity setting = null;
		Cursor c = db.rawQuery("select * from " + Constants.TABLE_SETTINGS
				+ " where settingkey=?",
				new String[] { settingkey });
	    
        while (c.moveToNext()) {
        	setting = new SettingEntity();
        	setting.set_id(c.getInt(c.getColumnIndex("_id")));  
        	setting.setSettingkey(c.getString(c.getColumnIndex("settingkey")));  
        	setting.setSettingvalue(c.getString(c.getColumnIndex("settingvalue")));
        }
        c.close();
        return setting;
	}
	public void UpdateSetting(SettingEntity setting)
	{
		if (setting == null)
			return;
	
		db.execSQL("update " + Constants.TABLE_SETTINGS + " set "
				+ TableSetting.COLUMN_SETTING_VALUE + "=? where "
				+ TableSetting.COLUMN_SETTING_KEY + "=?", new Object[] {
				setting.getSettingvalue(), setting.getSettingkey() });
	}
}
