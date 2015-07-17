package cn.qingguow.qingguoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
public class ArticleListActivity extends Activity {
	private int category_id;
	private TextView t1,t2,t3;
	private ViewPager mPager;
	private List<View> listViews;
	private ImageView cursor;
	private int bmpW;
	private int offset;
	private int currIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        Intent intent=getIntent();
        category_id=intent.getIntExtra("category_id", 0);

		setContentView(R.layout.activity_article_list);
		//初始化头标
		initTextView();
		//初始化下划线
		InitImageView();
		//初始化ViewPager
		initViewPager();
		
        //getMainNewsList();
        //openDetailActivity();
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
		t1=(TextView)findViewById(R.id.toptab1);
		t2=(TextView)findViewById(R.id.toptab2);
		t3=(TextView)findViewById(R.id.toptab3);
		t1.setOnClickListener(new MyOnClickListener(1));
		t2.setOnClickListener(new MyOnClickListener(2));
		t3.setOnClickListener(new MyOnClickListener(3));
	}
    public class MyOnClickListener implements View.OnClickListener{
    	private int index;
		public MyOnClickListener(int i) {
			this.index=i-1;
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
    	mPager=(ViewPager) findViewById(R.id.vPager);
    	listViews=new ArrayList<View>();
    	LayoutInflater mLayoutInflater=getLayoutInflater();
    	listViews.add(mLayoutInflater.inflate(R.layout.listview_article_list, null));
    	listViews.add(mLayoutInflater.inflate(R.layout.listview_article_list, null));
    	listViews.add(mLayoutInflater.inflate(R.layout.listview_article_list, null));
    	ListView view1= (ListView)listViews.get(0).findViewById(R.id.newsListView);
    	getMainNewsList(view1);
    	openDetailActivity(view1);
		mPager.setAdapter(new MyPagerAdapter(listViews));
    	mPager.setCurrentItem(0);
    	mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
    public class MyPagerAdapter extends PagerAdapter{

		private List<View> mListViews;

		public MyPagerAdapter(List<View> listViews) {
			this.mListViews=listViews;
		}
		 @Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			 ((ViewPager) arg0).removeView(mListViews.get(arg1));
		}
		@Override
		public int getCount() {
			return mListViews.size();
		}
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}
    	
    }
	/*
     * 获取主要的文章列表部分
     */
	private void getMainNewsList(ListView listView) {
		List<HashMap<String, Object>> data=getListData();
		
		String[] listKeyStrings= new String[]{"listMainTitle","listSubTitle"};
		int[] listItemId= new int[]{R.id.listMainTitle,R.id.listSubTitle};
		ListAdapter adapter = new SimpleAdapter(this, data, R.layout.listview_article_item,listKeyStrings,listItemId);
		listView.setAdapter(adapter);
	}
	/*
	 * 组合好要设置的数据
	 */
	private List<HashMap<String, Object>> getListData() {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		if(category_id==1){
			HashMap<String, Object> hashMap=new HashMap<String, Object>();
			hashMap.put("listMainTitle", "生活中遇到许多似曾相识的过客");
			hashMap.put("listSubTitle", "深觉这么近，却又那么远");
			hashMap.put("id", 1);
			data.add(hashMap);			
		}else{
			{
			HashMap<String, Object> hashMap=new HashMap<String, Object>();
			hashMap.put("listMainTitle", "引人注目的《仙剑奇侠传》电影正式宣布开拍2");
			hashMap.put("listSubTitle", "令玩家惊喜的是，演员阵容方面，由唐人方面宣布，目标集合电视剧版");
			hashMap.put("id", 2);
			data.add(hashMap);	
			}
			{
				HashMap<String, Object> hashMap=new HashMap<String, Object>();
				hashMap.put("listMainTitle", "引人注目的《仙剑奇侠传》电影正式宣布开拍3");
				hashMap.put("listSubTitle", "令玩家惊喜的是，演员阵容方面，由唐人方面宣布，目标集合电视剧版");
				hashMap.put("id", 3);
				data.add(hashMap);	
			}
			{
				HashMap<String, Object> hashMap=new HashMap<String, Object>();
				hashMap.put("listMainTitle", "引人注目的《仙剑奇侠传》电影正式宣布开拍4");
				hashMap.put("listSubTitle", "令玩家惊喜的是，演员阵容方面，由唐人方面宣布，目标集合电视剧版");
				hashMap.put("id", 4);
				data.add(hashMap);	
			}
			{
				HashMap<String, Object> hashMap=new HashMap<String, Object>();
				hashMap.put("listMainTitle", "引人注目的《仙剑奇侠传》电影正式宣布开拍5");
				hashMap.put("listSubTitle", "令玩家惊喜的是，演员阵容方面，由唐人方面宣布，目标集合电视剧版");
				hashMap.put("id", 5);
				data.add(hashMap);	
			}
			{
				HashMap<String, Object> hashMap=new HashMap<String, Object>();
				hashMap.put("listMainTitle", "引人注目的《仙剑奇侠传》电影正式宣布开拍6");
				hashMap.put("listSubTitle", "令玩家惊喜的是，演员阵容方面，由唐人方面宣布，目标集合电视剧版");
				hashMap.put("id", 6);
				data.add(hashMap);	
			}
		
		}	
		return data;
	}

	/*
	 * 查看详情
	 */
	public void openDetailActivity(ListView liseview){
		liseview.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, Object> paramHashMap=new HashMap<String, Object>();
				paramHashMap=(HashMap<String, Object>) arg0.getItemAtPosition(arg2);
				int articleId=(Integer) paramHashMap.get("id");
				Toast.makeText(ArticleListActivity.this, "我是详情,id:"+articleId, 1000).show();
				ArticleListActivity.this.startActivity(new Intent(ArticleListActivity.this,ArticleDetailActivity.class));
			}
		});
	}
    
}
