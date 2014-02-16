package com.jreader.myreader;
import java.io.File;
import java.util.List;
import com.jreader.myreader.R;
import com.jreader.common.FileHelper;
import com.jreader.entity.FileItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<FileItem> mData;

	public MyAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		
	}
	public MyAdapter(Context context,List<FileItem> data) {
		mInflater = LayoutInflater.from(context);
		mData = data;
	}
 	    
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		// convertView为null的时候初始化convertView。
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.dir_list_style, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.textView3);
			holder.details = (TextView) convertView.findViewById(R.id.txtDetail);			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		FileItem fi = mData.get(position);
		int icoId = FileHelper.LoadIcon(fi.file.getName());
		holder.img.setBackgroundResource(icoId);
		holder.title.setText(fi.file.getName());
		try {
			if (fi.file.isFile()) {
				String size = FileHelper.LoadFileSize(fi.file);
				holder.details.setText(size);
			}
			else
			{
				holder.details.setText(String.format("%s items", fi.file.listFiles().length));
			}

		} catch (Exception exp) {

		}
		return convertView;
	}

	public final class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView details;

	}
}
