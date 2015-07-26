package cn.qingguow.qingguoapp;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ArticleListActivity extends Activity {
	private int category_id;
	private TextView t1, t2, t3;
	private ViewPager mPager;
	private List<View> pages;
	private ImageView cursor;
	private int bmpW;
	private int offset;
	private int currIndex;
	private Handler handler;// 多线程处理Handler类对象

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去掉窗口标题栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 获取Intent对象
		Intent intent = this.getIntent();
		// 往Intent对象中放入数据,Int类型,键值对
		category_id = intent.getIntExtra("category_id", 0);
		// 设置视图界面activity_article_list
		this.setContentView(R.layout.activity_article_list);
		// 初始化头标
		initTextView();
		// 初始化下划线
		InitImageView();
		// 初始化ViewPager
		initViewPager();

		// getMainNewsList();
		// openDetailActivity();
	}

	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.line)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/*
	 * 初始化页头
	 */
	private void initTextView() {
		t1 = (TextView) findViewById(R.id.toptab1);
		t2 = (TextView) findViewById(R.id.toptab2);
		t3 = (TextView) findViewById(R.id.toptab3);
		t1.setOnClickListener(new MyOnClickListener(1));
		t2.setOnClickListener(new MyOnClickListener(2));
		t3.setOnClickListener(new MyOnClickListener(3));
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index;

		public MyOnClickListener(int i) {
			this.index = i - 1;
		}

		@Override
		public void onClick(View arg0) {
			mPager.setCurrentItem(index);
		}

	}

	/*
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		// 获取布局文件中的ViewPager对象
		mPager = (ViewPager) this.findViewById(R.id.vPager);

		// 加载页卡
		LayoutInflater mLayoutInflater = this.getLayoutInflater();
		View view0 = mLayoutInflater.inflate(R.layout.listview_article_list,
				null);
		View view1 = mLayoutInflater.inflate(R.layout.listview_article_list,
				null);
		View view2 = mLayoutInflater.inflate(R.layout.listview_article_list,
				null);

		// 设置适配器
		pages = new ArrayList<View>();
		pages.add(view0);
		pages.add(view1);
		pages.add(view2);
		PagerAdapter mPagerAdapter = new MyPagerAdapter(pages);

		// 设置每个页卡的列表
		ListView listview = (ListView) pages.get(0).findViewById(
				R.id.newsListView);
		this.getInternetData(listview);
		openDetailActivity(listview);

		mPagerAdapter.notifyDataSetChanged();
		mPager.setAdapter(mPagerAdapter);
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public class MyPagerAdapter extends PagerAdapter {

		private List<View> pages;

		public MyPagerAdapter(List<View> pages) {
			this.pages = pages;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pages.get(arg1));
		}

		@Override
		public int getCount() {
			return pages.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {

			((ViewPager) arg0).addView(pages.get(arg1), 0);
			return pages.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}


	/*
	 * 获取网络数据
	 */
	private void getInternetData(final ListView listView) {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					String json = (String) msg.obj;
					try {
						List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
						JSONArray jsonArray = new JSONArray(json);
						for (int i = 0; i < jsonArray.length(); i++) {
							HashMap<String, Object> hashMap = new HashMap<String, Object>();
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							hashMap.put("id", jsonObject.getInt("id"));
							hashMap.put("listSubTitle", "");
							hashMap.put("listMainTitle",
									jsonObject.getString("title"));
							data.add(hashMap);
						}
						// 设置适配器
						String[] listKeyStrings = new String[] {
								"listMainTitle", "listSubTitle" };
						int[] listItemId = new int[] { R.id.listMainTitle,
								R.id.listSubTitle };
						SimpleAdapter adapter = new SimpleAdapter(
								ArticleListActivity.this, data,
								R.layout.listview_article_item, listKeyStrings,
								listItemId);
						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();

					} catch (JSONException e) {
					}

				}
			}
		};
		new Thread() {
			public void run() {
				try {
					URL url = new URL(
							"http://www.qingguow.cn/index.php/index/getJsonArticle/");
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(10000);
					if (conn.getResponseCode() == 200) {
						InputStream in = conn.getInputStream();
						byte[] bytes = new byte[in.available()];
						int len = 0;
						String json = "";
						while ((len = in.read(bytes)) != -1) {
							json += new String(bytes);
						}
						Message msg = new Message();
						msg.what = 1;
						msg.obj = json;
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/*
	 * 查看详情
	 */
	public void openDetailActivity(ListView liseview) {
		liseview.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, Object> paramHashMap = new HashMap<String, Object>();
				paramHashMap = (HashMap<String, Object>) arg0
						.getItemAtPosition(arg2);
				int articleId = (Integer) paramHashMap.get("id");
				Toast.makeText(ArticleListActivity.this,
						"我是详情,id:" + articleId, 1000).show();
				ArticleListActivity.this.startActivity(new Intent(
						ArticleListActivity.this, ArticleDetailActivity.class));
			}
		});
	}

}
