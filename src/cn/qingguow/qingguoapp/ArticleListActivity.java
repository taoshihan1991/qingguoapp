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
		//��ʼ��ͷ��
		initTextView();
		//��ʼ���»���
		InitImageView();
		//��ʼ��ViewPager
		initViewPager();
		
        //getMainNewsList();
        //openDetailActivity();
    }
    private void InitImageView() {
    	 cursor = (ImageView) findViewById(R.id.cursor);
         bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.line)
                 .getWidth();// ��ȡͼƬ���
         DisplayMetrics dm = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(dm);
         int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
         offset = (screenW / 3 - bmpW) / 2;// ����ƫ����
         Matrix matrix = new Matrix();
         matrix.postTranslate(offset, 0);
         cursor.setImageMatrix(matrix);// ���ö�����ʼλ��
	}
    /**
     * ҳ���л�����
*/
    public class MyOnPageChangeListener implements OnPageChangeListener {

        int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
        int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����

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
            animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
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
     * ��ʼ��ҳͷ
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
     * ��ʼ��ViewPager
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
     * ��ȡ��Ҫ�������б���
     */
	private void getMainNewsList(ListView listView) {
		List<HashMap<String, Object>> data=getListData();
		
		String[] listKeyStrings= new String[]{"listMainTitle","listSubTitle"};
		int[] listItemId= new int[]{R.id.listMainTitle,R.id.listSubTitle};
		ListAdapter adapter = new SimpleAdapter(this, data, R.layout.listview_article_item,listKeyStrings,listItemId);
		listView.setAdapter(adapter);
	}
	/*
	 * ��Ϻ�Ҫ���õ�����
	 */
	private List<HashMap<String, Object>> getListData() {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

		if(category_id==1){
			HashMap<String, Object> hashMap=new HashMap<String, Object>();
			hashMap.put("listMainTitle", "�������������������ʶ�Ĺ���");
			hashMap.put("listSubTitle", "�����ô����ȴ����ôԶ");
			hashMap.put("id", 1);
			data.add(hashMap);			
		}else{
			{
			HashMap<String, Object> hashMap=new HashMap<String, Object>();
			hashMap.put("listMainTitle", "����עĿ�ġ��ɽ�����������Ӱ��ʽ��������2");
			hashMap.put("listSubTitle", "����Ҿ�ϲ���ǣ���Ա���ݷ��棬�����˷���������Ŀ�꼯�ϵ��Ӿ��");
			hashMap.put("id", 2);
			data.add(hashMap);	
			}
			{
				HashMap<String, Object> hashMap=new HashMap<String, Object>();
				hashMap.put("listMainTitle", "����עĿ�ġ��ɽ�����������Ӱ��ʽ��������3");
				hashMap.put("listSubTitle", "����Ҿ�ϲ���ǣ���Ա���ݷ��棬�����˷���������Ŀ�꼯�ϵ��Ӿ��");
				hashMap.put("id", 3);
				data.add(hashMap);	
			}
			{
				HashMap<String, Object> hashMap=new HashMap<String, Object>();
				hashMap.put("listMainTitle", "����עĿ�ġ��ɽ�����������Ӱ��ʽ��������4");
				hashMap.put("listSubTitle", "����Ҿ�ϲ���ǣ���Ա���ݷ��棬�����˷���������Ŀ�꼯�ϵ��Ӿ��");
				hashMap.put("id", 4);
				data.add(hashMap);	
			}
			{
				HashMap<String, Object> hashMap=new HashMap<String, Object>();
				hashMap.put("listMainTitle", "����עĿ�ġ��ɽ�����������Ӱ��ʽ��������5");
				hashMap.put("listSubTitle", "����Ҿ�ϲ���ǣ���Ա���ݷ��棬�����˷���������Ŀ�꼯�ϵ��Ӿ��");
				hashMap.put("id", 5);
				data.add(hashMap);	
			}
			{
				HashMap<String, Object> hashMap=new HashMap<String, Object>();
				hashMap.put("listMainTitle", "����עĿ�ġ��ɽ�����������Ӱ��ʽ��������6");
				hashMap.put("listSubTitle", "����Ҿ�ϲ���ǣ���Ա���ݷ��棬�����˷���������Ŀ�꼯�ϵ��Ӿ��");
				hashMap.put("id", 6);
				data.add(hashMap);	
			}
		
		}	
		return data;
	}

	/*
	 * �鿴����
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
				Toast.makeText(ArticleListActivity.this, "��������,id:"+articleId, 1000).show();
				ArticleListActivity.this.startActivity(new Intent(ArticleListActivity.this,ArticleDetailActivity.class));
			}
		});
	}
    
}
