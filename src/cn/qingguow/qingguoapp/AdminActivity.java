package cn.qingguow.qingguoapp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/*
 * ��̨��Activity
 */
public class AdminActivity extends Activity {
	private Handler handler;
	private EditText article_title;
	private EditText article_content;
	private String path = "http://www.qingguow.cn/test.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_admin);
		// ��ʼ���ؼ�
		article_title = (EditText) this.findViewById(R.id.article_title);
		article_content = (EditText) this.findViewById(R.id.article_content);
	}

	/*
	 * �������
	 */
	public void articleAdd(View v) {
		String title = article_title.getText().toString().trim();
		String content = article_content.getText().toString().trim();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("title", title);
		map.put("content", content);
		this.doPostData(map);
	}

	/*
	 * ����POST��������
	 */
	private void doPostData(
			final HashMap<String, ?> data) {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					String json = (String) msg.obj;
					try {
						JSONObject jsonObject = new JSONObject(json);
						String code=jsonObject.getString("code");
						String info=jsonObject.getString("info");
						if (code.equals("200")) {
							Toast.makeText(AdminActivity.this,info, 0).show();
						} else {
							Toast.makeText(AdminActivity.this, "����ʧ��", 0).show();
						}
						
					} catch (Exception e) {}

				}
			}
		};
		new Thread() {
			public void run() {
				try {
					String res = AdminActivity.this.setHttpPost(data);
					Message msg = new Message();
					msg.what = 1;
					msg.obj = res;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	private String setHttpPost(HashMap<String, ?> data) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		// ���ø���URLConnection�еķ���setDoOutput(),�������boolean,
		conn.setDoOutput(true);
		// ���ø���URLConnection�еķ���setUseCaches(),�������boolean,���Ի���
		conn.setUseCaches(false);
		OutputStream outputStream = conn.getOutputStream();
		// ѭ��ƴ��post���ַ���
		Set<String> keys = data.keySet();
		StringBuffer param = new StringBuffer();
		int flag = 0;
		for (String key : keys) {
			flag++;
			param.append(key);
			param.append("=");
			param.append(data.get(key));
			if (flag != keys.size()) {
				param.append("&");
			}
		}
		outputStream.write(param.toString().getBytes());
		outputStream.flush();
		outputStream.close();
		InputStream inputStream = conn.getInputStream();
		// ����byte[] ����,�����ֽ���������ֽڸ�����̬��ʼ��
		byte[] c = new byte[inputStream.available()];
		// ����InputStream�������read()����,���ֽ������ݶ�ȡ���ֽ���������
		inputStream.read(c);
		// ʵ����String��,�����ֽ�����,ת��String����
		return new String(c);
	}
}
