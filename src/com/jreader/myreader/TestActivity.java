package com.jreader.myreader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

public class TestActivity extends Activity {

	boolean isCreated = false;
	private Button btn;
	private View decView;
	PopupWindow mPopupWindow;
	EditText et;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);

		et = (EditText)findViewById(R.id.testTV);
		et.setText("123");
		
		 InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
         imm.hideSoftInputFromWindow(et.getWindowToken(),0); 
//		et.setSelected(true);
//		et.setLongClickable(true);
//		et.setOnLongClickListener(new View.OnLongClickListener() {
//			
//			public boolean onLongClick(View v) {
//				return true;
//			}
//		});
	}
	
	void createwindow() {
		Context mContext = TestActivity.this;
		LayoutInflater mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View music_popunwindwow = mLayoutInflater.inflate(R.layout.contentview,
				null);
		mPopupWindow = new PopupWindow(music_popunwindwow, 300, 50);

		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		isCreated = false;
		
		//super.onCreateContextMenu(menu, v, menuInfo);
		return true;
	}
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if (isCreated && mPopupWindow != null)
			mPopupWindow.showAtLocation(decView,
					Gravity.RIGHT | Gravity.BOTTOM, 0, 0);
		else
			isCreated = true;
		return false;
	}

}
