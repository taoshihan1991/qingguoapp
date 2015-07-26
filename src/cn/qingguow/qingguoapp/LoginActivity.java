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
 * ��½
 */
public class LoginActivity extends CommonActivity {
	private EditText login_username;
	private EditText login_password;
	private CheckBox login_remeber;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login);
		//��ʼ���ؼ�
		login_username=(EditText)this.findViewById(R.id.login_username);
		login_password=(EditText)this.findViewById(R.id.login_password);
		login_remeber=(CheckBox)this.findViewById(R.id.login_remeber);
		//��ȡ��ס���û���Ϣ
		Map<String, String> map=this.getUserInfo();
		if(map!=null){
			login_username.setText(map.get("username"));
			login_password.setText(map.get("password"));
		}
	}

	/*
	 * ��֤��½
	 */
	public void loginCheck(View v){
		String username=login_username.getText().toString().trim();
		String password=login_password.getText().toString().trim();
		if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
			Toast.makeText(this, "�û��������벻��Ϊ��", 1000).show();
		}else{
			//��ס����
			if(login_remeber.isChecked()){
				this.saveUserInfo(username,password);
			}
			if(username.equals("test")&&password.equals("123")){
				Toast.makeText(this, "��½�ɹ�", 0).show();
				this.startActivity(new Intent(this, AdminActivity.class));
			}else{
				Toast.makeText(this, "��½ʧ��,�û��������������", 0).show();
			}
		}
	}
	/*
	 * ��ס���뵽�ֻ�SD��
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
	 * ��ȡSD���е��û���Ϣ
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
