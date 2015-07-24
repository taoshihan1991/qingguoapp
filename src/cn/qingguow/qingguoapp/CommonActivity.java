package cn.qingguow.qingguoapp;

import android.app.Activity;
import android.view.View;
/*
 * 公共的Activity
 */
public class CommonActivity extends Activity {
	/*
	 * 返回上一页
	 */
    public void openPrevActivity(View v){
    	this.finish();
    }
}
