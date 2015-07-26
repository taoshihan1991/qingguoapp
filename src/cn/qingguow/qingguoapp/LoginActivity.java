package cn.qingguow.qingguoapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
/*
 * 登陆
 */
public class LoginActivity extends CommonActivity {
	private EditText login_username;
	private EditText login_password;
	private CheckBox login_remeber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login);
		//初始化控件
		login_username=(EditText)this.findViewById(R.id.login_username);
		login_password=(EditText)this.findViewById(R.id.login_password);
		login_remeber=(CheckBox)this.findViewById(R.id.login_remeber);
		//获取记住的用户信息
		Map<String, String> map=this.getUserInfo();
		if(map!=null){
			login_username.setText(map.get("username"));
			login_password.setText(map.get("password"));
		}
	}

	/*
	 * 验证登陆
	 */
	public void loginCheck(View v){
		String username=login_username.getText().toString().trim();
		String password=login_password.getText().toString().trim();
		if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
			Toast.makeText(this, "用户名或密码不能为空", 1000).show();
		}else{
			//记住密码
			if(login_remeber.isChecked()){
				this.saveUserInfo(username,password);
			}
			if(username.equals("test")&&password.equals("123")){
				Toast.makeText(this, "登陆成功", 0).show();
				this.startActivity(new Intent(this, AdminActivity.class));
			}else{
				Toast.makeText(this, "登陆失败,用户名或者密码错误", 0).show();
			}
		}
	}
	/*
	 * 记住密码到手机SD卡
	 */
	private boolean saveUserInfo(String username, String password) {
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return false;
		}
		File file=new File(Environment.getExternalStorageDirectory(),"test.txt");
		String data=username+"|"+password;
		try {
			FileOutputStream out=new FileOutputStream(file);
			out.write(data.getBytes());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/*
	 * 获取SD卡中的用户信息
	 * 
	 */
	private Map<String, String> getUserInfo() {
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return null;
		}
		File file=new File(Environment.getExternalStorageDirectory(),"test.txt");
		try {
			Map<String, String> map=new HashMap<String, String>();
			FileInputStream in=new FileInputStream(file);
			BufferedReader bReader=new BufferedReader(new InputStreamReader(in));
			String str=bReader.readLine();
			String[] arr=str.split("\\|");
			map.put("username", arr[0]);
			map.put("password", arr[1]);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
