package cn.qingguow.qingguoapp;

import cn.qingguow.qingguoapp.util.SystemUiHider;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class GuideActivity extends Activity {
	private ViewFlipper guideFlipper;//引导页滑动View
	private Animation inRightToLeftAnimation;//四个动画对象
	private Animation outRightToLeftAnimation;
	private Animation inLeftToRightAnimation;
	private Animation outLeftToRightAnimation;
	private float startX;			//手指按下起始坐标
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        
        //引导页滑动动画
        guideFlipper=(ViewFlipper)findViewById(R.id.guideFlipper);
        
		inRightToLeftAnimation=AnimationUtils.loadAnimation(this,R.anim.in_righttoleft);
		outRightToLeftAnimation=AnimationUtils.loadAnimation(this,R.anim.out_righttoleft);
		inLeftToRightAnimation=AnimationUtils.loadAnimation(this, R.anim.in_lefttoright);
		outLeftToRightAnimation=AnimationUtils.loadAnimation(this, R.anim.out_lefttoright);
		
        //开启新线程跑主页
        Thread guideThread=new mainThread();
        guideThread.start();
        
    }
    /*
     * 多线程跑新activity
     */
    final class mainThread extends Thread{
		public void run() {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent intent=new Intent(GuideActivity.this,MainActivity.class);
			startNewActivity(intent);
			GuideActivity.this.finish();
		}
    }
    /*
     * 重写滑动方法
     * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
     */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			startX=event.getX();
		}else if (event.getAction()==MotionEvent.ACTION_UP) {
			float endX=event.getX();
			if(startX>endX){
				guideFlipper.setInAnimation(inRightToLeftAnimation);
				guideFlipper.setOutAnimation(outRightToLeftAnimation);
				guideFlipper.showNext();
			}else if(startX<endX){
				guideFlipper.setInAnimation(inLeftToRightAnimation);
				guideFlipper.setOutAnimation(outLeftToRightAnimation);
				guideFlipper.showPrevious();
			}
		}
		return super.onTouchEvent(event);
	}
	/*
	 * 开启新activity
	 */
    protected void startNewActivity(Intent intent){
    	startActivity(intent);
    }  

}
