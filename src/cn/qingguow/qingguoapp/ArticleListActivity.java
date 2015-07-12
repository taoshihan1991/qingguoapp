package cn.qingguow.qingguoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


@SuppressLint("ShowToast") public class ArticleListActivity extends Activity {
	private int category_id;
	private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        Intent intent=getIntent();
        category_id=intent.getIntExtra("category_id", 0);

		setContentView(R.layout.activity_article_list);
        getMainNewsList();
        openDetailActivity();
    }
    /*
     * 获取主要的文章列表部分
     */
	private void getMainNewsList() {
		listView=(ListView)findViewById(R.id.newsListView);
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
	public void openDetailActivity(){
		listView.setOnItemClickListener(new OnItemClickListener() {
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
