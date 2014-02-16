package com.jreader.myreader;

import java.io.File;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Selection;
import android.util.FloatMath;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jreader.myreader.R;
import com.jreader.common.FileHelper;
import com.jreader.entity.ReadHistory;
import com.jreader.storage.ReadStorage;
@SuppressLint("DefaultLocale")
public class ReadActivity extends FragmentActivity {

	private static Context context;
	
	private ReadStorage storage;
	private ReadHistory history=null;
	private SectionsPagerAdapter mSectionsPagerAdapter;
    private static ViewPager mViewPager;    
    private final static String TAG="ReadBook";
    private File file = null;    
	private String fileContent;	
	private static String fileName;
	private int PAGE_SIZE = 1000;
	private static int PAGE_COUNT=10;
	private static int CURRENT_PAGE = 0;
	private boolean isCreated = false;
	private LayoutInflater inflater=null;
	private PopupWindow mPopupWindow = null;
	private View decorView;
	private SeekBar seekbar;
	private View floatView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	context = getApplicationContext();
    	inflater = LayoutInflater.from(context);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);
        //createMyOptionsMenu();
        decorView  = this.getWindow().getDecorView();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());        
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        ActionBar bar = getActionBar();
        bar.hide();
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int position) {
				
				//每翻一页，就记录下当前所读页数
				if (storage != null && history != null) {
					history.setPage(position);
					storage.UpdateReadHistory(history);
				}				
				seekbar.setProgress(position + 1);
			}

			public void onPageScrollStateChanged(int state) {
			}

			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}
		});
        file = (File)getIntent().getSerializableExtra(Constants.File_Key);
        
        if(file==null)
        	return;
        
        fileName = file.getName();
        this.setTitle(fileName);
		fileContent = FileHelper.ReadFileContent2(file);
		
		PAGE_COUNT = (int) Math.ceil((double)fileContent.length() / (double)PAGE_SIZE);
		this.setTitle(String.format("%s",file.getName()));
		
		storage = new ReadStorage(this);
		history = storage.QueryReadHistory(file.getName(), file.getPath());
		
		//判断是否有阅读记录，有则打开历史记录并加载到上次浏览的页数
		//否则创建一个新的阅读记录
		
		wrapFloatView();
		if(history == null)
		{
			history = new ReadHistory();
			history.setBookName(file.getName());
			history.setPath(file.getPath());
			history.setPage(0);
			storage.InsertReadHistory(history);
		}
		else
		{
			CURRENT_PAGE = history.getPage();
			mViewPager.setCurrentItem(CURRENT_PAGE);
		}	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	isCreated = false;
    	createFloatWindow(floatView);
//        getMenuInflater().inflate(R.menu.activity_read_book, menu);  
//        return true;
//    	 menu.add("menu");// 必须创建一项
//         return super.onCreateOptionsMenu(menu);    
    	//createFloatWindow();
    	return true;
    }
    
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		if(isCreated)
		{
			if(!mPopupWindow.isShowing())
			{
				mPopupWindow.showAtLocation(decorView, Gravity.BOTTOM, 0, 0);
			}			
		}
		else
		{
			isCreated = true;
		}
		
		return false;
	}

	public void wrapFloatView() {
		LayoutInflater mLayoutInflater = (LayoutInflater) context
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		floatView = mLayoutInflater.inflate(R.layout.menuitemview, null);
		seekbar = (SeekBar) floatView.findViewById(R.id.seekBar1);
		seekbar.setMax(PAGE_COUNT - 1);
		seekbar.setProgress(1);
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			public void onStopTrackingTouch(SeekBar seekBar) {
				// tvPage.setVisibility(View.INVISIBLE);
				CURRENT_PAGE = seekBar.getProgress();
				// tvPage.setTextSize(Constants.Page_inactive_size);
				mViewPager.setCurrentItem(CURRENT_PAGE);
				// tvPage.setTextColor(Constants.Page_inactive_color);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// if(tvPage.getVisibility() == View.INVISIBLE)
				// tvPage.setVisibility(View.VISIBLE);
				// tvPage.setText(String.format("%d/%d", progress + 1,
				// PAGE_COUNT));
				// tvPage.setTextSize(Constants.Page_active_size);
				// tvPage.setTextColor(Constants.Page_active_color);

			}
		});

	}

    public void createFloatWindow(View floatView)
    {
		if (this.mPopupWindow == null) {

			this.mPopupWindow  = new PopupWindow(floatView, this.decorView.getWidth(), 50,
					true);
			//mPopupWindow.setAnimationStyle(Animation.)
			// mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
			// 设置半透明灰色
			
			ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
			this.mPopupWindow .setBackgroundDrawable(dw);
			this.mPopupWindow .setOutsideTouchable(true);
			this.mPopupWindow .setClippingEnabled(true);
		}			
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}



		@Override
		public Fragment getItem(int i) {
			
			String currentStr = "";
			if (fileContent.length() > PAGE_SIZE * (i + 1)) {
				currentStr = fileContent.substring(i * PAGE_SIZE, (i + 1)
						* PAGE_SIZE);
			} else {
				currentStr = fileContent.substring(i * PAGE_SIZE);
			}
			
			//createFloatWindow(i + 1);
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
			args.putString(DummySectionFragment.ARG_CONTENT, currentStr);
			fragment.setArguments(args);

			return fragment;
		}

		@Override
		public int getCount() {
			return PAGE_COUNT;
		}

		@SuppressLint("DefaultLocale")
		@Override
		public CharSequence getPageTitle(int position) {

			return String.format("Page %d", position);
		}
	}

	@SuppressLint("FloatMath")
	public static class DummySectionFragment extends Fragment {
		private float size = 1.1f;
		private float mult = 1.3f;
		public DummySectionFragment() {
		}

		public static final String ARG_SECTION_NUMBER = "section_number";
		public static final String ARG_CONTENT = "file_content";
		static final float TEXT_SIZE = 20f;
		public float CalcDist(MotionEvent event)
		{
			float x = event.getX(0)-event.getX(1);
			float y = event.getY(0)-event.getY(1);
			return FloatMath.sqrt(x*x-y*y);
		}
		
		public void Zoomin(TextView view) {
			float currentSize = view.getTextSize() - 5;
			Log.i(TAG, String.format("%f", currentSize));
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX,currentSize);
		}

		public void Zoomout(TextView view) {
			float currentSize = view.getTextSize() + 5;
			Log.i(TAG, String.format("%f", currentSize));
			
			view.setTextSize(TypedValue.COMPLEX_UNIT_PX,currentSize);
		}
		


		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {		
			Bundle args = getArguments();
			int number = args.getInt(DummySectionFragment.ARG_SECTION_NUMBER);
			ContentView cv = new ContentView(context);
//			ScrollView sv = (ScrollView)cv.findViewById(R.id.scrollView1);
//			sv.setVerticalScrollBarEnabled(true);

			final TextView tvHeader = (TextView)cv.GetHeader();
			tvHeader.setText(fileName);

			final TextView tvPage = (TextView)cv.GetPageView();
			tvPage.setText(String.format("%d/%d", number,PAGE_COUNT));
			
			

			
			final EditText contentText = cv.GetTextView();
//			contentText.setOnTouchListener(new View.OnTouchListener() {
//				static final int NONE = 0;// 空
//				static final int DRAG = 1;// 按下第一个点
//				static final int ZOOM = 2;// 按下第二个点
//				int mode = NONE;
//				float oldDist;
//				int currentScale=0;
//				public boolean onTouch(View v, MotionEvent event) {
//					switch (event.getAction() & MotionEvent.ACTION_MASK) {
//					case MotionEvent.ACTION_DOWN:
//						mode = DRAG;
//						
//						break;
//					case MotionEvent.ACTION_UP:
//					case MotionEvent.ACTION_POINTER_UP:
//						mode = NONE;
//						break;
//					case MotionEvent.ACTION_POINTER_DOWN:
//						oldDist = CalcDist(event);
//						if (oldDist > 10f) {
//							mode = ZOOM;
//						}
//						break;
//					case MotionEvent.ACTION_MOVE:
//						if (mode == ZOOM) {
//							// 正在移动的点距初始点的距离
//							float newDist = CalcDist(event);
//
//							int od = (int) oldDist;
//							int nd = (int) newDist;
//							Log.i(TAG, String.format("%d %d", od, nd));
//
//							int t = (nd - od) / 100;
//							// currentScale = t > currentScale ? t :
//							// currentScale;
//
//							if (t > currentScale) {
//								Log.i(TAG, "Zoomout");
//								Zoomout(contentText);
//								currentScale = t;
//							}
//							else if(t < currentScale){
//								Log.i(TAG, "Zoomin");
//								Zoomin(contentText);
//								currentScale = t;
//							}
//
//						}
//						break;
//					}
//					return true;
//				}
//			});
			contentText.setGravity(Gravity.LEFT);
			
			String content = args.getString(ARG_CONTENT);
			contentText.setPadding(20, 20, 20, 20);
			contentText.setTextSize(TEXT_SIZE);
			contentText.setText(content);
			contentText.setLineSpacing(0, mult);
			contentText.setTextScaleX(size);

			return cv;
		}

		@SuppressWarnings("deprecation")
		public void dumpEvent(MotionEvent event) {
			String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
					"POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
			StringBuilder sb = new StringBuilder();
			int action = event.getAction();
			int actionCode = action & MotionEvent.ACTION_MASK;
			sb.append("event ACTION_").append(names[actionCode]);
			if (actionCode == MotionEvent.ACTION_POINTER_DOWN
					|| actionCode == MotionEvent.ACTION_POINTER_UP) {
				sb.append("(pid ").append(
						action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
				sb.append(")");
			}
			sb.append("[");
			for (int i = 0; i < event.getPointerCount(); i++) {
				sb.append("#").append(i);
				sb.append("(pid ").append(event.getPointerId(i));
				sb.append(")=").append((int) event.getX(i));
				sb.append(",").append((int) event.getY(i));
				if (i + 1 < event.getPointerCount())
					sb.append(";");
			}
			sb.append("]");
			Log.d("ReadBook", sb.toString());
		}

	}

}
