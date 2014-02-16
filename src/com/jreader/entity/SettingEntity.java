package com.jreader.entity;

public class SettingEntity {

	private int _id;
	private String _settingkey;
	private String _settingvalue;

	public SettingEntity()
	{}
	
	public SettingEntity(String settingkey,String settingvalue)
	{
		_settingkey = settingkey;
		_settingvalue = settingvalue;
	}
	
	public String getSettingkey() {
		return _settingkey;
	}

	public void setSettingkey(String settingkey) {
		this._settingkey = settingkey;
	}

	public String getSettingvalue() {
		return _settingvalue;
	}

	public void setSettingvalue(String settingvalue) {
		this._settingvalue = settingvalue;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

}
