package com.jreader.myreader;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.jreader.myreader.R;

import com.jreader.common.FileAssistent;
import com.jreader.common.FileWrapper;
import com.jreader.entity.FileItem;
import com.jreader.entity.SettingEntity;
import com.jreader.storage.SettingStorage;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	private List<FileItem> items = null;// 存放名称
	private List<String> paths = null;// 存放路径
	private String rootPath = "/";
	private TextView tv;
	private Button btnUp;
	private Button btnRoot;

	private SettingStorage setStorage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		super.onCreate(savedInstanceState);
		this.setTitle("MyReader");
		setContentView(R.layout.activity_main);
		tv = (TextView) this.findViewById(R.id.TextView);
		btnUp = (Button) this.findViewById(R.id.btnUp);
		btnRoot = (Button) this.findViewById(R.id.btnRoot);
		btnUp.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String currentPath = (String) tv.getText();
				int i = currentPath.lastIndexOf("/");
				currentPath = currentPath.substring(0, i);
				if (currentPath.equals(""))
					navigateDir(rootPath);
				else
					navigateDir(currentPath);

			}
		});
		btnRoot.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				navigateDir(rootPath);
			}
		});
		boolean openLastDir = false;
		setStorage = new SettingStorage(this);
		SettingEntity lastDir = setStorage
				.QuerySetting(com.jreader.common.Constants.SETTINGS_LASTDIR);

		if (lastDir != null && !lastDir.getSettingvalue().isEmpty()) {

			File f = new File(lastDir.getSettingvalue());
			if (f.exists())
				openLastDir = true;
		}

		if (openLastDir) {
			navigateDir(lastDir.getSettingvalue());
		} else {
			navigateDir(rootPath);
		}
	}

	public void navigateDir(String dir) {
		try {
			this.tv.setText(dir);
			items = new ArrayList<FileItem>();
			paths = new ArrayList<String>();
			File f = new File(dir);
			
			File[] files = f.listFiles();
			// if (!dir.equals(rootPath)) {
			// items.add(Constants.Dir_root);
			// paths.add(Constants.Dir_root);
			// items.add(Constants.Dir_up);
			// paths.add(f.getParent());
			// }
			if (files != null) {
				
				List<FileWrapper> fileWrappers = new ArrayList<FileWrapper>();
				for (int i = 0; i < files.length; i++) {					
					if(files[i].isHidden())
						continue;					
					fileWrappers.add(new FileWrapper(files[i]));
				}
				
				FileWrapper[] fwArray = new FileWrapper[fileWrappers.size()];
				for (int i = 0; i < fileWrappers.size(); i++) {
					fwArray[i] = fileWrappers.get(i);
				}
				
				Arrays.sort(fwArray);
				for (int i = 0; i < fwArray.length; i++) {
					File currentFile = fwArray[i].getFile();					
					FileItem item = new FileItem();
					item.file = currentFile;
					items.add(item);
					paths.add(currentFile.getPath());
				}
			}
			MyAdapter adapter = new MyAdapter(this, items);
			this.getListView().setAdapter(adapter);
			this.getListView().setItemsCanFocus(false);

			SettingEntity se = new SettingEntity(
					com.jreader.common.Constants.SETTINGS_LASTDIR, dir);

			setStorage.UpdateSetting(se);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String path = paths.get(position);
		File file = new File(path);

		// 如果是文件夹就继续分解
		if (file.isDirectory()) {
			this.navigateDir(path);
		} else {
			String ext = FileAssistent.getExtension(file.getName());
			if (ext.equalsIgnoreCase("txt"))
				OpenText(file);
		}
	}

	public void OpenText(File txt) {
		Intent mIntent = new Intent(MainActivity.this, ReadActivity.class);
		//Intent mIntent = new Intent(MainActivity.this, TestActivity.class);

		Bundle mBundle = new Bundle();
		mBundle.putSerializable(Constants.File_Key, txt);
		mIntent.putExtras(mBundle);
		startActivity(mIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);

		return true;
	}
}
