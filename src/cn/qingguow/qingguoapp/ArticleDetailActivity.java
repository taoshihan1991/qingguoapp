package cn.qingguow.qingguoapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class ArticleDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_article_detail);
    }
    public void openPrevActivity(View v){
    	finish();
    }
    
}
