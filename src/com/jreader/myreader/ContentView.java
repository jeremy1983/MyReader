package com.jreader.myreader;

import com.jreader.myreader.R;
import com.jreader.myreader.R.id;
import com.jreader.myreader.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class ContentView extends LinearLayout{

	public EditText GetTextView()
	{
		return (EditText)findViewById(R.id.tvContent);
	}
//	public SeekBar GetSeekBar()
//	{
//		return (SeekBar)findViewById(R.id.seekBar1);
//	}
	public TextView GetPageView()
	{
		return (TextView)findViewById(R.id.tvPage);
	}
	public TextView GetHeader()
	{
		return (TextView)findViewById(R.id.tvHeader);
	}
	public ContentView(Context context) {
		super(context);
        LayoutInflater  mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.contentview, this, true);
	}
}
