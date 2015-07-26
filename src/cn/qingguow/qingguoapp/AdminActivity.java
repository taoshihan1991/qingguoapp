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
 * 后台主Activity
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
		// 初始化控件
		article_title = (EditText) this.findViewById(R.id.article_title);
		article_content = (EditText) this.findViewById(R.id.article_content);
	}

	/*
	 * 添加文章
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
	 * 处理POST网络数据
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
							Toast.makeText(AdminActivity.this, "发布失败", 0).show();
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
		// 调用父类URLConnection中的方法setDoOutput(),传入参数boolean,
		conn.setDoOutput(true);
		// 调用父类URLConnection中的方法setUseCaches(),传入参数boolean,忽略缓存
		conn.setUseCaches(false);
		OutputStream outputStream = conn.getOutputStream();
		// 循环拼接post的字符串
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
		// 声明byte[] 数组,按照字节流里面的字节个数动态初始化
		byte[] c = new byte[inputStream.available()];
		// 调用InputStream类里面的read()方法,把字节流内容读取到字节数组里面
		inputStream.read(c);
		// 实例化String类,传入字节数组,转成String类型
		return new String(c);
	}
}
