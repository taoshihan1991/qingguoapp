package cn.qingguow.qingguoapp;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements OnCheckedChangeListener {
	private Intent intent;
	private TabHost tabHost;
	private TabSpec tabSpec;
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        //初始化按钮
       initRadio();
        setTabHost();

    }
    private void initRadio() {
    	((RadioButton) findViewById(R.id.radio_button0)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button1)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button2)).setOnCheckedChangeListener(this);
        ((RadioButton) findViewById(R.id.radio_button3)).setOnCheckedChangeListener(this);
	}

	private void setTabHost(){
    	tabHost=getTabHost();
    	setUpIntent();
    }
	private void setUpIntent() {
    	intent=new Intent(this,ArticleListActivity.class);
    	intent.putExtra("category_id", 1);
		tabSpec=tabHost.newTabSpec("one").setIndicator("主页").setContent(intent);
		tabHost.addTab(tabSpec);
		
		intent=new Intent(this,ArticleListActivity.class);
		intent.putExtra("category_id", 2);
		tabSpec=tabHost.newTabSpec("two").setIndicator("社区").setContent(intent);
		tabHost.addTab(tabSpec);
		
		intent=new Intent(this,ArticleListActivity.class);
    	intent.putExtra("category_id", 1);
		tabSpec=tabHost.newTabSpec("three").setIndicator("主页").setContent(intent);
		tabHost.addTab(tabSpec);

		tabHost.setCurrentTab(0);
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
            switch (buttonView.getId()) {
            case R.id.radio_button0:
            	tabHost.setCurrentTabByTag("one");
                break;
            case R.id.radio_button1:
            	tabHost.setCurrentTabByTag("two");
                break;
            case R.id.radio_button2:
            	tabHost.setCurrentTabByTag("three");
                break;
            }
        }
	}
	/*
	 * 登陆界面
	 */
	public void loginAction(View v){
		Intent intent=new Intent(this,LoginActivity.class);
		this.startActivity(intent);
	}

}
